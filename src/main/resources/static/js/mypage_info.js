let index = {
    init: function() {
        $("#btn").on("click", (event) => {
            event.preventDefault(); // 폼 전송 방지
            this.update(); // 회원 정보 수정 함수 호출 변경
        });
    },

    // 회원 정보 수정 기능
    update: function() {
        let pwd = $("#pass").val();
        let data = {
            name: $("#user").val(),
            phone: $("#phone").val(),
            id: $("#nickname").val(), // 사용자 식별 정보로 사용될 수 있음
            pwd: pwd, // 비밀번호 확인 절차 없이 비밀번호 직접 사용
            email: $("#email").val()
        };
        console.log('pwd= ' + pwd);
        console.log(data);

        $.ajax({
            type: "POST",
            url: "/auth/updateMember", // 회원 정보 수정을 위해 기존 회원가입 엔드포인트 재사용
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(resp) {
                // alert("회원 정보가 수정되었습니다.");
                Swal.fire({
                    title: "수정 성공",
                    text: "회원 정보가 수정되었습니다.",
                    icon: "success",
                    confirmButtonColor: "#A2DBA2DD"
                });
                location.href = "/"; // 수정 후 리다이렉션될 페이지 변경 가능
            },
            error: function(xhr, status, error) {
                console.error(xhr.responseText); // 실패 시 에러 로그 출력
                // alert("회원 정보 수정에 실패했습니다. 다시 시도해주세요.");
                Swal.fire({
                    title: "수정 실패",
                    text: "회원 정보 수정에 실패했습니다. 다시 시도해주세요.",
                    icon: "error",
                    confirmButtonColor: "#ffcbad"
                });
            }
        });
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
        } else {
            $(this).attr('class',"fa fa-eye fa-lg")
                .prev('input').attr('type','password');
        }
    });
});
