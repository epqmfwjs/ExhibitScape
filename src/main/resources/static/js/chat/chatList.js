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
// 모달 가져오기
var modal = document.getElementById("myModal");

// 모달을 여는 버튼
var btn = document.getElementById("myBtn");

// 모달을 닫는 <span> 요소 (x 버튼)
var span = document.getElementsByClassName("close")[0];

btn.onclick = function() {
    modal.style.display = "block";
}

span.onclick = function() {
    modal.style.display = "none";
}

// 사용자가 모달 외부를 클릭하면 모달 닫기
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