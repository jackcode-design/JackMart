// Floating chat widget -> POST /api/v1/chat (canonical)
(function () {
  function base() {
    const p = window.location.pathname.split('/');
    return p.length > 1 && p[1] ? '/' + p[1] : '';
  }
  document.addEventListener('click', async (e) => {
    if (e.target.id === 'chatBtn') document.getElementById('chatPanel').hidden = false;
    if (e.target.id === 'chatClose') document.getElementById('chatPanel').hidden = true;
    if (e.target.id === 'chatSend') {
      const inp = document.getElementById('chatText');
      const body = document.getElementById('chatBody');
      const msg = inp.value.trim();
      if (!msg) return;
      body.innerHTML += '<div><b>You:</b> ' + msg.replace(/</g, '&lt;') + '</div>';
      inp.value = '';
      const res = await fetch(base() + '/api/v1/chat', {
        method: 'POST', headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ message: msg })
      });
      const json = await res.json();
      const reply = json.data && json.data.reply ? json.data.reply : 'Offline right now.';
      body.innerHTML += '<div><b>JackMart:</b> ' + reply.replace(/</g, '&lt;') + '</div>';
      body.scrollTop = body.scrollHeight;
    }
  });
})();
