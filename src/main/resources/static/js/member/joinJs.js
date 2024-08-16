$(document).ready(function() {
    $("form").submit(function(e) {
        let isChecked = isIdChecked && isNicknameChecked && isEmailChecked;
        if (!isChecked) {
            if (!isIdChecked) {
                alert("아이디 중복체크를 해주세요.");
            }
            if (!isNicknameChecked) {
                alert("닉네임 중복체크를 해주세요.");
            }
            if (!isEmailChecked) {
                alert("이메일 중복체크를 해주세요.");
            }
            e.preventDefault();
            return false;
        }

        if (!validateMemberId()) {
            e.preventDefault();
            return false;
        }
        if (!validatePassword()) {
            e.preventDefault();
            return false;
        }
        if (!validateNickname()) {
            e.preventDefault();
            return false;
        }
        if (!validateEmail()) {
            e.preventDefault();
            return false;
        }
    });

    $("#idcheck").click(function(e) {
        e.preventDefault();
        checkMemberId();
    });

    $("#nicknamecheck").click(function(e) {
        e.preventDefault();
        checkNickname();
    });

    $("#emailcheck").click(function(e) {
        e.preventDefault();
        checkEmail();
    });
});

let isIdChecked = false;
let isNicknameChecked = false;
let isEmailChecked = false;

function enableJoinButton() {
    if (isIdChecked && isNicknameChecked && isEmailChecked) {
        $("#join-submit").prop("disabled", false);
    } else {
        $("#join-submit").prop("disabled", true);
    }
}

function validateMemberId() {
    var memberId = $("#memberId").val().trim();

    if (memberId === "") {
        alert("아이디를 입력해주세요.");
        $("#memberId").focus();
        return false;
    }

    var memberIdPattern = /^[a-z0-9]{4,12}$/;
    if (!memberIdPattern.test(memberId)) {
        alert("아이디는 영문 소문자와 숫자 4~12자리여야 합니다.");
        $("#memberId").focus();
        return false;
    }
    return true;
}

function validatePassword() {
    var password1 = $("#password1").val().trim();
    var password2 = $("#password2").val().trim();

    if (password1 === "") {
        alert("비밀번호를 입력해주세요.");
        $("#password1").focus();
        return false;
    }

    if (password2 === "") {
        alert("비밀번호 확인은 필수입니다.");
        $("#password2").focus();
        return false;
    }

    if (password1 !== password2) {
        alert("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        $("#password2").focus();
        return false;
    }
    return true;
}

function validateNickname() {
    var nickname = $("#nickname").val().trim();

    if (nickname === "") {
        alert("닉네임을 입력해주세요.");
        $("#nickname").focus();
        return false;
    }
    return true;
}

function validateEmail() {
    var email = $("#email").val().trim();

    if (email === "") {
        alert("이메일을 입력해주세요.");
        $("#email").focus();
        return false;
    }

    var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(email)) {
        alert("이메일 형식이 올바르지 않습니다.");
        $("#email").focus();
        return false;
    }
    return true;
}

function checkMemberId() {
    var memberId = $("#memberId").val().trim();

    if (memberId === "") {
        alert("아이디를 입력해주세요.");
        $("#memberId").focus();
        return;
    }

    var memberIdPattern = /^[a-z0-9]{4,12}$/;
    if (!memberIdPattern.test(memberId)) {
        alert("아이디는 영문 소문자와 숫자 4~12자리여야 합니다.");
        $("#memberId").focus();
        return;
    }

    $.ajax({
        type: "POST",
        url: "/idcheck",
        contentType: "application/json",
        data: JSON.stringify({ memberId: memberId }),
        success: function(response) {
            if (response) {
                alert("사용 가능한 아이디입니다.");
                isIdChecked = true;
            } else {
                alert("이미 사용하고 있는 ID입니다.");
                $("#memberId").val("").focus();
                isIdChecked = false;
            }
            enableJoinButton();
        },
        error: function() {
            console.log("아이디 중복 확인 중 오류가 발생했습니다.");
            isIdChecked = false;
            enableJoinButton();
        }
    });
}

function checkNickname() {
    var nickname = $("#nickname").val().trim();

    if (nickname === "") {
        alert("닉네임을 입력해주세요.");
        $("#nickname").focus();
        return;
    }

    $.ajax({
        type: "POST",
        url: "/nicknamecheck",
        contentType: "application/json",
        data: JSON.stringify({ nickname: nickname }),
        success: function(response) {
            if (response) {
                alert("사용 가능한 닉네임입니다.");
                isNicknameChecked = true;
            } else {
                alert("이미 사용하고 있는 닉네임입니다.");
                $("#nickname").val("").focus();
                isNicknameChecked = false;
            }
            enableJoinButton();
        },
        error: function() {
            console.log("닉네임 중복 확인 중 오류가 발생했습니다.");
            isNicknameChecked = false;
            enableJoinButton();
        }
    });
}

function checkEmail() {
    var email = $("#email").val().trim();

    if (email === "") {
        alert("이메일을 입력해주세요.");
        $("#email").focus();
        return;
    }

    var emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(email)) {
        alert("이메일 형식이 올바르지 않습니다.(ㅁㅁ@ㅁㅁ.ㅁㅁ)");
        $("#email").focus();
        return;
    }

    $.ajax({
        type: "POST",
        url: "/emailcheck",
        contentType: "application/json",
        data: JSON.stringify({ email: email }),
        success: function(response) {
            if (response) {
                alert("사용 가능한 이메일입니다.");
                isEmailChecked = true;
            } else {
                alert("이미 사용하고 있는 이메일입니다.");
                $("#email").val("").focus();
                isEmailChecked = false;
            }
            enableJoinButton();
        },
        error: function() {
            console.log("이메일 중복 확인 중 오류가 발생했습니다.");
            isEmailChecked = false;
            enableJoinButton();
        }
    });
}