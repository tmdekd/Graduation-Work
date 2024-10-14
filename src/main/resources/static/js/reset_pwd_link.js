let index = {
    init: function() {
        console.log("reset_pwd_link.js");
        $("#btn_save").on("click", (event) => {
            this.reset();
        });
    },

    // 비밀번호 재설정 기능
    reset: function() {
        let pwd = $("#pwd").val();
        let pwdChk = $("#pwdChk").val();
        let data = {};
        console.log('pwd= ' + pwd);
        console.log('pwdChk= ' + pwdChk);
        if(pwd === pwdChk) {
            data={
                id: $("#nickname").val(),
                pwd: $("#pwd").val()
            };
            console.log(data);
        } else {
            // alert("비밀번호와 비밀번호 확인란이 일치하지 않습니다.");
            Swal.fire({
                title: "비밀번호와 비밀번호 확인란이 일치하지 않습니다.",
                icon: "error",
                confirmButtonColor: "#ffcbad"
            });
            return;
        }

        $.ajax({
            type: "POST",
            url: "/auth/resetPwd",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            beforeSend: function() {
                console.log("비밀번호 재설정 요청 데이터:", data);
            },
            success: function (resp) {
                // alert("비밀번호 재설정이 완료되었습니다.\n 다시 로그인 해주세요.");
                Swal.fire({
                    title: "비밀번호 재설정 성공",
                    text: "다시 로그인 해주세요.",
                    icon: "success",
                    confirmButtonColor: "#A2DBA2DD"
                });
                console.log("응답:", resp);
                location.href = "/auth/login_joinForm";
            },
            error: function(xhr, status, error) {
                console.error("응답 텍스트:", xhr.responseText);
                console.log("XHR 객체:", xhr);
                console.log("상태:", status);
                console.log("오류:", error);
                alert("비밀번호 재설정에 실패했습니다. 다시 시도해주세요.");
                Swal.fire({
                    title: "비밀번호 재설정 실패",
                    text: "다시 시도해주세요.",
                    icon: "error",
                    confirmButtonColor: "#ffcbad"
                });
            }
        });

    }

};

index.init();

$(document).ready(function() {
    console.log("메서드 실행");
    $('.group i').on('click', function () {
        $('input').toggleClass('active');
        if ($('input').hasClass('active')) {
            $(this).attr('class', "fa fa-eye-slash fa-lg")
                .prev('input').attr('type', "text");
        } else {
            $(this).attr('class', "fa fa-eye fa-lg")
                .prev('input').attr('type', 'password');
        }
    })
})