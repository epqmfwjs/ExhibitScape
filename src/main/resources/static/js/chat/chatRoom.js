        document.addEventListener('DOMContentLoaded', (event) => {
            const urlParams = new URLSearchParams(window.location.search);
            const roomId = urlParams.get('roomId'); // URL의 roomId 파라미터 값
            const sender = document.getElementById('sender').value; // 입력된 사용자 이름을 가져옴
            const chatRoomNo = roomId;
            console.log("sender 값이야 "+sender)
            loadAndShowParticipants();
            if (!roomId || !sender) {
                console.error("roomId 하고 sender 가 같은 곳 을 안봐.");
                return;
            }
            //로컬스토리지 에서 대화내용 가져오게하는 함수 호출
            loadMessagesFromLocal(roomId);

            function loadAndShowParticipants() {
                fetch(`/chat/participants?roomId=${roomId}`)
                    .then(response => response.json())
                    .then(participants => {
                        console.log("데이터 타입확인해보자 " + typeof participants); // 데이터 타입 확인
                        console.log("배열인지 왁인해보자" + Array.isArray(participants)); // participants가 배열인지 확인
                        if (participants == null) {
                            console.log("updateParticipantsList null 이니?");
                        }
                        if (!Array.isArray(participants)) {
                            console.error("Participants가 배열형태로 넘어오지않음:", participants);
                            participants = [];
                        }
                        console.log("updateParticipantsList 반환은 하는구나");
                        updateParticipantsList(participants);
                    })
                    .catch(error => console.error("불러오기실패한듯....:", error));
            }

            function updateParticipantsList(participants) {
                console.log("받아온 participants 데이터: ", participants);
                const participantsList = document.getElementById('participants');
                participantsList.innerHTML = ''; // 기존 목록을 비움

                for (const participant of participants) {
                    const div = document.createElement('div');
                    div.textContent = participant; // 참여자 이름 설정
                    div.className = 'participant'; // CSS 클래스 추가
                    participantsList.appendChild(div);
                }
            }

                    // WebSocket 연결 설정
                    //var socket = new SockJS(`http://localhost:8085/ws/chat?roomId=${roomId}`);

                    async function connectToSocket(roomId) {
                        try {
                            // API 호출하여 socketUrl 가져오기
                            const response = await fetch('/api/socket-url');
                            const socketUrl = `${await response.text()}?roomId=${roomId}`;

                            // SockJS 연결 생성
                            var socket = new SockJS(socketUrl);
                            var stompClient = Stomp.over(socket);

                            // 연결에 대한 추가 로직...
                        } catch (error) {
                            console.error('Error connecting to socket:', error);
                        }
                    }

                    stompClient.connect({roomId: roomId}, function (frame) {
                        console.log('Connected: ' + frame);
                        stompClient.subscribe(`/topic/room/${roomId}`, function (message) {
                            showMessage(JSON.parse(message.body));
                        });
                        //뒤로가기방지
                        window.history.forward(1);

                        window.addEventListener("beforeunload", function() {
                        if (typeof stompClient !== 'undefined') {
                            leaveChat(stompClient);
                            console.log('뒤로가기 실행시 요걸로 커넥션열결이 끊길까?: ');
                        }
                    });


                    // 채팅방 입장 메시지 보내기
                    stompClient.send(`/app/chat.sendMessage`, {}, JSON.stringify({messageNo: chatRoomNo, sender: sender, content: '님이 채팅방에 입장하셨습니다.'}));
                    });

                    document.querySelector('#leave').addEventListener('click', function () {
                        leaveChat(stompClient);
                    });

                    document.querySelector('#send').addEventListener('click', function () {
                        sendMessage(stompClient, chatRoomNo, sender);
                    });

            // 메시지 전송 함수 수정
            function sendMessage(stompClient, chatRoomNo, sender) {
                var content = document.getElementById('message').value; // 입력된 메시지 내용을 가져옴

                if (!content) {
                    console.error("전송할 내용 없음");
                    return;
                }




                //뒤로가기방지
                window.history.forward(1);

                // HTML 태그 제거
                content = content.replace(/</g, "&lt;").replace(/>/g, "&gt;");

                var message = JSON.stringify({messageNo: chatRoomNo, sender: sender, content: content});

                 // 메시지 로컬 스토리지에 저장
                  saveMessageToLocal(chatRoomNo, sender, content);

                stompClient.send('/app/chat.sendMessage', {}, message);

                // 입력창 초기화
                document.getElementById('message').value = '';
                }

                // 메시지를 로컬 스토리지에 저장하는 함수
                function saveMessageToLocal(roomId, sender, message) {
                  const existingMessages = JSON.parse(localStorage.getItem(`chat_${roomId}`)) || [];
                  existingMessages.push({ sender, message });
                  localStorage.setItem(`chat_${roomId}`, JSON.stringify(existingMessages));
                }

                // 'send' 버튼 클릭 이벤트 핸들러 수정
                document.querySelector('#send').addEventListener('click', function () {
                    sendMessage(stompClient, chatRoomNo, sender);
                });

                // 엔터키 누르면 메시지 전송
                document.getElementById('message').addEventListener('keyup', function(event) {
                    if (event.key === 'Enter') {
                        sendMessage(stompClient, chatRoomNo, sender);
                    }
                });

            function showMessage(message) {
                var messageElement = document.createElement('div');

                // 현재 시간을 '시:분 AM/PM' 형식으로 포맷팅
                var now = new Date();
                var hours = now.getHours();
                var minutes = now.getMinutes();
                var ampm = hours >= 12 ? '오후' : '오전';
                hours = hours % 12;
                hours = hours ? hours : 12; // 0시는 12시로 변환
                minutes = minutes < 10 ? '0'+minutes : minutes;
                var strTime = ampm + ' ' + hours + ':' + minutes;

                // 메시지 내용에 시간 추가background-color: rgb(255, 255, 255);
                messageElement.innerHTML = '<strong style="font-size: 0.7.rem; margin-bottom: 15px;">'+message.sender+'<strong>'+'<br/>' + '<span style="font-size: 1rem;">'+ message.content +'</span>'+ ' <span style="font-size: 0.5rem;">' + strTime + '</span>';
                messageElement.classList.add('message'); // 공통 클래스 추가

                if (message.sender === sender) {
                    messageElement.classList.add('message-sent');
                } else {
                    messageElement.classList.add('message-received');
                }

                var chatContainer = document.getElementById('chat-container');
                var scrollCahttingMap = document.getElementById('leftarea')
                //chatContainer.scrollTop = chatContainer.scrollHeight;
                chatContainer.appendChild(messageElement); // 메시지를 컨테이너에 추가
                loadAndShowParticipants();
                // 스크롤을 최신 메시지 위치로 이동
                scrollCahttingMap.scrollTop = chatContainer.scrollHeight;

            }
            function leaveChat(stompClient) {
                if (stompClient && stompClient.connected) {
                    stompClient.send(`/app/chat.sendMessage`, {}, JSON.stringify({
                        roomId: roomId,
                        messageNo: chatRoomNo,
                        sender: sender,
                        content: '님이 채팅방에서 나가셨습니다.'
                    }));

                    stompClient.disconnect(function () {
                        console.log("커넥션 컷.");
                        loadDeleteParticipants();
                        window.location.href = '/chatList';

                    });
                }
            }
            function loadDeleteParticipants() {
            // 여기서 ChatRestController로 요청을 ㄱㄱ
            fetch('/chat/deleteParticipants', {
                method: 'POST', // 요청 메소드
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    roomId: roomId,
                    sender: sender
                })
            })
            .then(response => {
                if (!response.ok) {

                    throw new Error('네트워크 응답이 올바르지 않습니다.');
                }
                return response.json();
            })
            .then(data => {
                console.log('참가자 삭제 요청 성공:', data);

            })
            .catch(error => {
                console.error('참가자 삭제 요청 실패:', error);
            });

        }
            function loadMessagesFromLocal(roomId) {
              const messages = JSON.parse(localStorage.getItem(`chat_${roomId}`)) || [];
              const messageContainer = document.getElementById('chat-container'); // 메시지를 표시할 컨테이너의 ID를 'messageContainer'라고 가정
              messages.forEach(msg => {
                const messageElement = document.createElement('div');
                messageElement.innerHTML = `<strong>${msg.sender}</strong>: ${msg.message}`;
                messageContainer.appendChild(messageElement);
              });

        };
        });