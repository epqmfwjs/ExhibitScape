
//댓글 수정 비동기반영 로직 
function editComment(button) {
    const no = button.getAttribute('data-no');
    const commentC_no = button.getAttribute('data-comment-no');

    const commentElement = document.getElementById('comment-' + commentC_no);

    const commentContent = commentElement.textContent;

    const newContent = prompt('수정하실 내용을 입력해 주세요.:', commentContent);
    
    // newContent가 비어있지 않은 경우
    if (newContent) {
       // ajax에  요청 주소와 전송방식 등 설정 지정 
        $.ajax({
            url: `/review/board/${no}/comment/${commentC_no}/update`,
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({ content: newContent }),
            //요청 성공과 실패 시 로직 구현
            success: function(data) {
               //댓글 수정이 바로 반영이되도록하는 로직
               commentElement.textContent = newContent;
    		},
            error: function(xhr, status, error) {
                console.error('에러에러에러:', error);
            }
    	});
    }
}

//삭제구현
function confirmDelete(no, C_no) {
    var confirmResponse = confirm('댓글을 삭제 하시겠습니까?');
    if(confirmResponse) {
        window.location.href = '/review/board/' + no + '/comment/' + C_no + '/remove?no=' + no + '&C_no=' + C_no;
    }
}

//댓글 작성 공백 로직
function validateForm() {
    var content = document.forms["commentForm"]["content"].value;
    if (content == null || content.trim() == "") {
        alert("내용을 입력해주세요.");
        return false;  // 폼 요청 거부
    }
    return true;  // 폼 요청 허용
}

//토글 버튼 뷰페이지 결정로직
document.addEventListener("DOMContentLoaded", function() {
    var view1 = document.getElementById("view1");
    var view2 = document.getElementById("view2");

    // 페이지 로드 시 로컬 스토리지에서 상태를 확인
    var toggleState = localStorage.getItem("toggleState") || "view1";
    
    // 뷰 상태에 따라 보여주기
    showView(toggleState);

    // 토글 버튼 이벤트 리스너
    document.getElementById("toggleButton1").addEventListener("click", function() {
        toggleView();
    });

    document.getElementById("toggleButton2").addEventListener("click", function() {
        toggleView();
    });

    // 토글 버튼 애니메이션
    addToggleButtonAnimation("toggleButton1");
    addToggleButtonAnimation("toggleButton2");

});

// 뷰 상태에 따라 view1과 view2를 토글
function toggleView() {
    var currentView = localStorage.getItem("toggleState") || "view1";
    var nextView = currentView === "view1" ? "view2" : "view1"; // 현재 view 상태가 1인지 아닌지 확인하여 삼항연산자 사용 보여줄 페이지 지정
    localStorage.setItem("toggleState", nextView);
    showView(nextView);
}

// 주어진 상태에 따라 뷰를 출력
function showView(view) {
    var view1 = document.getElementById("view1");
    var view2 = document.getElementById("view2");
    if (view === "view2") {
        view1.style.display = "none";
        view2.style.display = "block";
    } else {
        view1.style.display = "block";
        view2.style.display = "none";
    }
}

// 버튼 클릭에 따른 애니메이션 추가
function addToggleButtonAnimation(buttonId) {
    // buttonId를 통해 버튼 요소를 가져옴
    const button = document.getElementById(buttonId);
    // 버튼에 클릭 이벤트 리스너를 추가
    button.addEventListener('click', () => {
        // 버튼을 움직일 초기 위치 설정
        let position = 0;
        // 25ms마다 실행되는 인터벌 설정
        const interval = setInterval(() => {
            // position 값을 10씩 증가시키며 버튼을 오른쪽으로 이동
            position += 10;
            // CSS transform 속성을 이용해 버튼의 위치를 업데이트
            button.style.transform = `translateX(${position}px)`;
            // position이 50에 도달하면 인터벌을 멈춤
            if (position >= 50) {
                clearInterval(interval);
                // 250ms 후 버튼을 원래 위치로 돌림
                setTimeout(() => {
                    button.style.transform = 'translateX(0)';
                }, 250);
            }
        }, 25);
    });
}

// 리스트일때 검색기능

document.addEventListener('DOMContentLoaded', function() {
    // 키워드 검색 버튼 이벤트 리스너
    var searchButton = document.getElementById('listSearchButtonKeyword');
    if (searchButton) {
        searchButton.addEventListener('click', submitForm);
    }

    // 날짜 범위 검색 버튼 이벤트 리스너
    var searchDateButton = document.getElementById('listSearchButtonDate');
    if (searchDateButton) {
        searchDateButton.addEventListener('click', submitFormDate);
    }
});

function submitForm() {
    var keyword = document.forms["searchForm"]["listKeyword"].value;
    
    if (keyword == null || keyword.trim() == "") {
        alert("검색어를 입력해주세요.");
        return false;
    }
    document.getElementById("searchForm").submit();
    return false;
}

function submitFormDate() {
    var listStart_date = document.getElementById('listStart_date').value;
    var listEnd_date = document.getElementById('listEnd_date').value;
    
    if (!listStart_date || !listEnd_date) {
        alert('시작 날짜와 종료 날짜를 입력해주세요.');
        return false;
    }
    document.getElementById("searchFormDate").submit();
    return false;
}

//캘린더 구현(FullCalendar사용) 
document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'ko',
        // 여기에 FullCalendar 설정 추가
    });
    calendar.render();

	 // 처음 로드 시 이벤트 데이터를 불러와 캘린더에 표시해줌
	loadAndDisplayEvents(calendar);
	
    // 이벤트 불러오기 버튼에 클릭 이벤트 리스너 추가
    $('#toggleButton2').click(function() {
       
       // loadAndDisplayEvents(calendar); //이방법은 클릭 이벤트 발생시 캘리더를 불러옴 하지만 초기 빈 캘린더 데이터를 불러와서 요청을 한번더 해야 데이터가 정상적으로보여짐.
       
        location.reload();//클릭이벤트 발생시 페이지 새로고침효과로 데이터 불러오는 역활  이렇게 하는게 맞는 방법인지는 아직 모르겟음...
    
    });
});

//캘린더 스위칭 시 보여줄 전체 리스트 조회로직
document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'ko',
    });

    calendar.render();

    // 처음 로드 시 모든 이벤트 데이터를 불러와 캘린더에 표시
    loadAndDisplayEvents(calendar);

    // 이벤트 불러오기 버튼 클릭 시
    document.getElementById('toggleButton2').addEventListener('click', function() {
        loadAndDisplayEvents(calendar);
    });

    // 날짜 기반 검색 버튼 이벤트 리스너
    document.getElementById('calendarSearchButtonDate').addEventListener('click', function() {
        var start_date = document.getElementById('calendarStart_date').value;
        var end_date = document.getElementById('calendarEnd_date').value;

        if (start_date && end_date) {
            loadAndDisplayEvents(calendar, {start_date: start_date, end_date: end_date});
        } else {
            alert('시작 날짜와 종료 날짜를 입력해주세요.');
        }
    });

    // 키워드 검색 버튼 이벤트 리스너
    document.getElementById('calendarSearchButtonKeyword').addEventListener('click', function(event) {
        event.preventDefault(); // 폼의 기본 제출 동작을 방지
        var keyword = document.getElementById('calendarKeyword').value.trim();

        if (keyword === "") {
            alert("검색어를 입력해주세요.");
        } else {
            loadAndDisplayEvents(calendar, {keyword: keyword}, true);
        }
    });

    // 인기 검색어 클릭 이벤트 
    document.querySelectorAll('.popular ul li a').forEach(function(element) {
        element.addEventListener('click', function(event) {
            event.preventDefault(); // 링크의 기본 동작을 방지
            var keyword = this.textContent.trim();
            loadAndDisplayEvents(calendar, {keyword: keyword}, true);
        });
    });
});

function loadAndDisplayEvents(calendar, searchParams, isKeywordSearch) {
    var params = searchParams || {};
    var url = isKeywordSearch ? "/calendar/calendarSearch" : (Object.keys(params).length ? "/calendar/calendarDate" : "/calendar/allCalendar");

    $.ajax({
        url: url,
        type: "GET",
        dataType: "json",
        data: params,
        success: function(data) {
            var events = data.map(function(event) {
                return {
                    id: event.no,
                    title: event.title,
                    start: event.start_date,
                    url: "/scheduleBoard/detail/" + event.no,
                };
            });
            calendar.removeAllEvents();
            calendar.addEventSource(events);
        },
        error: function(xhr, status, error) {
            console.error("로딩 실패", status, error);
        }
    });
}

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
