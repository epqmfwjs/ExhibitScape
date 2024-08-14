//샌드버드 챗봇
!function(w, d, s, ...args){
  var div = d.createElement('div');
  div.id = 'aichatbot';
  d.body.appendChild(div);
  w.chatbotConfig = args;
  var f = d.getElementsByTagName(s)[0],
  j = d.createElement(s);
  j.defer = true;
  j.type = 'module';
  j.src = 'https://aichatbot.sendbird.com/index.js';
  f.parentNode.insertBefore(j, f);
}(window, document, 'script', '5E02B55C-3A20-42C8-815B-4BDEFDDF0C76', 'onboarding_bot', {
  apiHost: 'https://api-cf-ap-2.sendbird.com',
});