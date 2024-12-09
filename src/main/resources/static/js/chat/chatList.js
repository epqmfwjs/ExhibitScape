// 페이지가 로드될 때 실행될 함수
window.onload = function() {
    // 로컬 스토리지를 이용하여 새로고침 여부를 확인
    if (!localStorage.getItem('firstLoad')) {
        // 새로고침이 필요한 경우
        localStorage.setItem('firstLoad', 'true');
        window.location.reload();
    } else {
        // 새로고침 후 로컬 스토리지의 값을 제거하여 다음 로드 때 새로고침하지 않음
        localStorage.removeItem('firstLoad');
        //window.location.reload();
    }
}

var modal = document.getElementById("myModal");


var btn = document.getElementById("myBtn");


var span = document.getElementsByClassName("close")[0];

btn.onclick = function() {
    modal.style.display = "block";
}

span.onclick = function() {
    modal.style.display = "none";
}

window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}

document.getElementById('createRoom').onsubmit = function(event) {
    var roomName = document.getElementById('roomName').value.trim();
    if (roomName === '') {
        alert('채팅방 이름에 공백을 사용할 수 없습니다.');
        event.preventDefault();
    }
}