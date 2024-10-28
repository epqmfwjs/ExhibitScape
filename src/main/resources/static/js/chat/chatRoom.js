document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const roomId = urlParams.get('roomId');
    const sender = document.getElementById('sender').value;
    const chatRoomNo = roomId;

    if (!roomId || !sender) return;

    loadAndShowParticipants();
    loadMessagesFromLocal(roomId);
    connectToSocket(roomId);

    document.querySelector('#leave').addEventListener('click', () => leaveChat(stompClient));
    document.querySelector('#send').addEventListener('click', () => sendMessage(stompClient, chatRoomNo, sender));
    document.getElementById('message').addEventListener('keyup', (event) => {
        if (event.key === 'Enter') sendMessage(stompClient, chatRoomNo, sender);
    });

    // 나가기 , 새로고침시 경고창
    window.addEventListener("beforeunload", (event) => {
        event.preventDefault();
        event.returnValue = '';
    });

    // 소켓 연결
    async function connectToSocket(roomId) {
        try {
            const response = await fetch('/api/socket-url');
            const socketUrl = `${await response.text()}?roomId=${roomId}`;
            const socket = new SockJS(socketUrl);
            stompClient = Stomp.over(socket);

            stompClient.connect({}, (frame) => {
                console.log('Connected: ' + frame);
                stompClient.subscribe(`/topic/room/${roomId}`, (message) => showMessage(JSON.parse(message.body)));

                stompClient.send(`/app/chat.sendMessage`, {}, JSON.stringify({
                    messageNo: chatRoomNo,
                    sender: sender,
                    content: '님이 채팅방에 입장하셨습니다.'
                }));
            });
        } catch (error) {
            console.error('Error connecting to socket:', error);
        }
    }

    function loadAndShowParticipants() {
        fetch(`/chat/participants?roomId=${roomId}`)
            .then(response => response.json())
            .then(participants => {
                updateParticipantsList(Array.isArray(participants) ? participants : []);
            })
            .catch(error => console.error("불러오기 실패:", error));
    }

    function updateParticipantsList(participants) {
        const participantsList = document.getElementById('participants');
        participantsList.innerHTML = ''; // 기존 목록 초기화
        participants.forEach(participant => {
            const div = document.createElement('div');
            div.textContent = participant;
            div.className = 'participant';
            participantsList.appendChild(div);
        });
    }

    function sendMessage(stompClient, chatRoomNo, sender) {
        const content = document.getElementById('message').value.trim();
        if (!content) {
            console.error("전송할 내용 없음");
            return;
        }

        // HTML 태그 제거
        const safeContent = content.replace(/</g, "&lt;").replace(/>/g, "&gt;");
        const message = JSON.stringify({ messageNo: chatRoomNo, sender: sender, content: safeContent });

        saveMessageToLocal(chatRoomNo, sender, safeContent);
        stompClient.send('/app/chat.sendMessage', {}, message);
        document.getElementById('message').value = ''; // 입력창 초기화
    }

    function saveMessageToLocal(roomId, sender, message) {
        const existingMessages = JSON.parse(localStorage.getItem(`chat_${roomId}`)) || [];
        existingMessages.push({ sender, message });
        localStorage.setItem(`chat_${roomId}`, JSON.stringify(existingMessages));
    }

    function showMessage(message) {
        const messageElement = document.createElement('div');
        const now = new Date();
        const strTime = `${now.getHours() % 12 || 12}:${now.getMinutes().toString().padStart(2, '0')} ${now.getHours() >= 12 ? '오후' : '오전'}`;

        messageElement.innerHTML = `<strong style="font-size: 0.7rem; margin-bottom: 15px;">${message.sender}</strong><br/> <span style="font-size: 1rem;">${message.content}</span> <span style="font-size: 0.5rem;">${strTime}</span>`;
        messageElement.classList.add('message', message.sender === sender ? 'message-sent' : 'message-received');

        const chatContainer = document.getElementById('chat-container');
        chatContainer.appendChild(messageElement);
        chatContainer.scrollTop = chatContainer.scrollHeight; // 스크롤을 최신 메시지 위치로 이동

        loadAndShowParticipants();
    }

    function leaveChat(stompClient) {
        if (stompClient && stompClient.connected) {
            stompClient.send(`/app/chat.sendMessage`, {}, JSON.stringify({
                roomId: roomId,
                messageNo: chatRoomNo,
                sender: sender,
                content: '님이 채팅방에서 나가셨습니다.'
            }));

            stompClient.disconnect(() => {
                console.log("커넥션 컷.");
                loadDeleteParticipants();
                window.location.href = '/chatList';
            });
        }
    }

    function loadDeleteParticipants() {
        fetch('/chat/deleteParticipants', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ roomId: roomId, sender: sender })
        })
            .then(response => {
                if (!response.ok) throw new Error('네트워크 응답이 올바르지 않습니다.');
                return response.json();
            })
            .then(data => console.log('참가자 삭제 요청 성공:', data))
            .catch(error => console.error('참가자 삭제 요청 실패:', error));
    }

    function loadMessagesFromLocal(roomId) {
        const messages = JSON.parse(localStorage.getItem(`chat_${roomId}`)) || [];
        const messageContainer = document.getElementById('chat-container');
        messages.forEach(msg => {
            const messageElement = document.createElement('div');
            messageElement.innerHTML = `<strong>${msg.sender}</strong>: ${msg.message}`;
            messageContainer.appendChild(messageElement);
        });
    }
});
