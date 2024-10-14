let index = {
    init: function() {
        console.log("reset_pwd.js")
        $("#btn_save").on("click", (event) => {
            event.preventDefault(); // 폼 전송 방지
            this.resetPassword();
        });
    },

    resetPassword: function() {
        let id = document.getElementById('nickname').value.trim();
        let email = document.getElementById('email').value.trim();

        if(id === "") {
            // alert("아이디를 입력해주세요.");
            Swal.fire({
                icon: "error",
                title: "아이디를 입력해주세요.",
                confirmButtonColor: "#ffcbad"
            });
            return;
        }

        if(email === "") {
            // alert("이메일을 입력해주세요.");
            Swal.fire({
                icon: "error",
                title: "이메일을 입력해주세요.",
                confirmButtonColor: "#ffcbad"
            });
            return;
        }

        let emailRegex = /^(([^<>()\[\]\\.,;:\s@\"]+(\.[^<>()\[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        if(!emailRegex.test(email)) {
            // alert("유효하지 않은 이메일 형식입니다.");
            Swal.fire({
                icon: "error",
                title: "유효하지 않은 이메일 형식입니다.",
                confirmButtonColor: "#ffcbad"
            });
            return;
        }

        // $.ajax({
        //     type: "POST",
        //     url: "/auth/send_reset_email",
        //     contentType: "application/json",
        //     data: JSON.stringify({ id: id, email: email }),
        //     success: function(response) {
        //         alert("비밀번호 재설정 링크가 이메일로 전송되었습니다.");
        //         alert("메일이 발송되지 않았다면, 입력란을 다시 확인해주세요.")
        //     },
        //     error: function(xhr, status, error) {
        //         alert("오류 발생: " + xhr.responseText);
        //     }
        // });
        $.ajax({
            type: "POST",
            url: "/auth/send_reset_email",
            contentType: "application/json",
            data: JSON.stringify({ id: id, email: email }),
            beforeSend: function() {
                // AJAX 요청을 보내기 전에 로딩바를 표시
                $("#loadingBar").show();
            },
            success: function(response) {
                // alert("비밀번호 재설정 링크가 이메일로 전송되었습니다.");
                // alert("메일이 발송되지 않았다면, 입력란을 다시 확인해주세요.");
                Swal.fire({
                    icon: "success",
                    title: "비밀번호 재설정 링크가 이메일로 전송되었습니다.",
                    html: "메일이 발송되지 않았다면, <br>입력란을 다시 확인해주세요.",
                    confirmButtonColor: "#A2DBA2DD"
                });
            },
            error: function(xhr, status, error) {
                // alert("오류 발생: " + xhr.responseText);
                Swal.fire({
                    icon: "error",
                    title: "오류 발생",
                    confirmButtonColor: "#ffcbad"
                });
            },
            complete: function() {
                // 요청이 완료되면(성공 또는 실패) 로딩바를 숨김
                $("#loadingBar").hide();
            }
        });
    }
};

index.init();
