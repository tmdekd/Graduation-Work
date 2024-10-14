let index = {


    init: function() {
        $("#btn_save").on("click", (event) => {
            event.preventDefault(); // 폼 전송 방지
            this.save();
        });
        $("#nickname").on("blur", () => {
            this.checkUserId(); // 아이디 입력란을 벗어날 때 중복 체크 수행
        });
    },

    // 회원가입 기능
    save: function() {
        let pwd = $("#pwd").val();
        let pwdChk = $("#pwdChk").val();
        let data = {};
        console.log('pwd= ' + pwd);
        console.log('pwdChk= ' + pwdChk);
        if(pwd === pwdChk) {
            data={
                name: $("#name").val(),
                phone: $("#phone").val(),
                id: $("#nickname").val(),
                pwd: $("#pwd").val(),
                email: $("#email").val()
            };
            console.log(data);
        } else {
            // alert("비밀번호와 비밀번호 확인란이 일치하지 않습니다.");
            Swal.fire({
                icon: "error",
                title: "회원가입에 실패했습니다!",
                html: "비밀번호와 비밀번호 확인란이<br>일치하지 않습니다.",
                confirmButtonColor: "#ffcbad"
            });
            return;
        }

        $.ajax({
            type: "POST",
            url: "/auth/joinProc",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (resp) {
                // alert("회원가입이 완료되었습니다.");
                Swal.fire({
                    icon: "success",
                    html: "회원가입이 <br>완료되었습니다.",
                    confirmButtonColor: "#A2DBA2DD"
                }).then((result) => {
                    if(result.value) {
                        location.href = "/auth/login_joinForm";
                    }
                })
            },
            error: function(xhr, status, error) {
                console.error(xhr.responseText); // 실패 시 에러 로그 출력
                Swal.fire({
                    icon: "error",
                    title: "회원가입에 실패했습니다!",
                    text: "다시 시도해 주세요",
                    confirmButtonColor: "#ffcbad"
                });
            }
        });
    },

    // 아이디 중복체크 기능
    checkUserId: function() {
        let userId = $("#nickname").val();
        console.log(userId);
        console.log(typeof userId);
        if(userId !== '') {
            $.ajax({
                type: "GET",
                url: "/auth/checkUserId?userId=" + userId,
                success: function (resp) {
                    if (resp === true) {
                        // alert("사용할 수 있는 아이디입니다.");
                        Swal.fire({
                            icon: "success",
                            title: "아이디 중복체크 완료",
                            text: "사용할 수 있는 아이디입니다.",
                            confirmButtonColor: "#A2DBA2DD"
                        });
                    } else {
                        // alert("이미 사용 중인 아이디입니다. 다른 아이디를 입력해주세요.");
                        Swal.fire({
                            icon: "error",
                            title: "아이디 중복체크 완료",
                            html: "이미 사용 중인 아이디입니다. <br>다른 아이디를 입력해주세요.",
                            confirmButtonColor: "#ffcbad"
                        });
                    }
                },
                error: function(xhr, status, error) {
                    console.error(xhr.responseText); // 에러 로그 출력
                    // alert("아이디 중복 확인 중에 오류가 발생했습니다. 다시 시도해주세요.");
                    Swal.fire({
                        icon: "error",
                        title: "아이디 중복체크 오류",
                        text: "다시 시도해주세요.",
                        confirmButtonColor: "#ffcbad"
                    });
                }
            });
        } else {
            // alert("아이디를 입력해주세요.");
            Swal.fire({
                icon: "error",
                title: "아이디 중복체크 오류",
                text: "아이디를 입력해주세요.",
                confirmButtonColor: "#ffcbad"
            });
        }
    }
};

index.init();

$(document).ready(function(){
    console.log("메서드 실행");
    $('.group i').on('click',function(){
        $('input').toggleClass('active');
        if($('input').hasClass('active')){
            $(this).attr('class',"fa fa-eye-slash fa-lg")
                .prev('input').attr('type',"text");
        }else{
            $(this).attr('class',"fa fa-eye fa-lg")
                .prev('input').attr('type','password');
        }
    }),

    // 페이지 로드 시 localStorage에서 아이디 값을 가져와서 아이디 입력 필드에 설정
    $('#id').val(localStorage.getItem('savedId'));

    // 로그인 버튼 클릭 이벤트
    $('#btn_login').click(function() {
        // 체크박스 체크 여부 확인
        if ($('#check').is(':checked')) {
            // 체크되어 있으면 아이디 값을 localStorage에 저장
            localStorage.setItem('savedId', $('#id').val());
        } else {
            // 체크되어 있지 않으면 localStorage의 아이디 값을 삭제
            localStorage.removeItem('savedId');
        }
    });
});