package com.js.jsmart.service.chat;

import com.js.jsmart.util.ConfigUtil;
import java.util.HashMap;
import java.util.Map;

/**
 * Chat service: provider selection (Factory), rate limit, length cap, per-session cache.
 * The repeat-response cache is stored in the caller's session map, so different
 * sessions never share cached replies.
 */
public class ChatService {
  /** Maximum accepted message length; longer input is truncated. */
  public static final int MAX_MESSAGE_LENGTH = 500;
  /** Maximum messages accepted per session within the rate-limit window. */
  public static final int MAX_MESSAGES_PER_MINUTE = 10;
  /** Sliding rate-limit window in milliseconds. */
  public static final long RATE_LIMIT_WINDOW_MS = 60_000L;
  private static final String HITS_KEY = "chatHits";
  private static final String CACHE_KEY = "chatCache";
  private static final int DEFAULT_TIMEOUT_MS = 8000;

  private final ChatProvider provider;

  /** Build from config ai.chatbot.provider. */
  public ChatService() {
    String name = ConfigUtil.get("ai.chatbot.provider", "mock");
    int timeout;
    try {
      timeout = Integer.parseInt(ConfigUtil.get("gemini.api.timeoutMs", "8000"));
    } catch (NumberFormatException e) {
      timeout = DEFAULT_TIMEOUT_MS;
    }
    if ("gemini".equalsIgnoreCase(name)) {
      this.provider = new GeminiChatProvider(timeout);
    } else {
      this.provider = new MockChatProvider();
    }
  }

  /** Test constructor. */
  public ChatService(ChatProvider provider) {
    this.provider = provider;
  }

  /**
   * Reply with guards.
   * @param session session map holding rate-limit hits and the per-session reply cache
   * @param message user message
   * @param context product context
   * @return reply
   */
  public String reply(Map<String, Object> session, String message, String context) {
    if (message == null) {
      message = "";
    }
    message = message.trim();
    if (message.length() > MAX_MESSAGE_LENGTH) {
      message = message.substring(0, MAX_MESSAGE_LENGTH);
    }
    long now = System.currentTimeMillis();
    @SuppressWarnings("unchecked")
    java.util.List<Long> hits = (java.util.List<Long>) session.computeIfAbsent(HITS_KEY,
        k -> new java.util.ArrayList<Long>());
    hits.removeIf(t -> now - t > RATE_LIMIT_WINDOW_MS);
    if (hits.size() >= MAX_MESSAGES_PER_MINUTE) {
      return "Rate limit: please wait a moment (10 messages/minute).";
    }
    hits.add(now);
    @SuppressWarnings("unchecked")
    Map<String, String> cache = (Map<String, String>) session.computeIfAbsent(CACHE_KEY,
        k -> new HashMap<String, String>());
    String key = message.toLowerCase();
    if (cache.containsKey(key)) {
      return cache.get(key);
    }
    String out = provider.getReply(message, context);
    cache.put(key, out);
    return out;
  }
}
