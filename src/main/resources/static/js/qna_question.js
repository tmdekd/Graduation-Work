// 240529 기존코드(아래부터)
$(document).ready(function(){
    $('#write_button').click(function(){
        var title = $('#question_input').val(); // 제목 입력 필드의 값
        var content = $('#question_textarea').val(); // 내용 입력 필드의 값

        if (!title) { // 제목이 입력되지 않은 경우
            Swal.fire({
                icon: "error",
                title: "제목을 입력하세요.",
                confirmButtonColor: "#ffcbad"
            });
            return; // Ajax 요청을 보내지 않고 함수 종료
        }

        if (!content) { // 제목이 입력되지 않은 경우
            Swal.fire({
                icon: "error",
                title: "내용을 입력하세요.",
                confirmButtonColor: "#ffcbad"
            });
            return; // Ajax 요청을 보내지 않고 함수 종료
        }


        $.ajax({
            url: '/qna/qna_question', // 게시글을 등록하는 서버의 URL
            type: 'POST',
            contentType: 'application/json', // JSON 형식의 데이터를 서버로 전송하기 위한 설정
            data: JSON.stringify({
                // title: $('#question_input').val(), // 제목 입력 필드의 ID가 question_input이므로 이를 사용
                // content: $('#question_textarea').val(), // 내용 입력 필드의 ID가 question_textarea이므로 이를 사용
                title: title,
                content: content,
                // memberId와 같은 사용자 정보는 서버 측에서 세션 등을 통해 처리합니다.
            }),
            success: function(response) {
                Swal.fire({
                    icon: "success",
                    title: "게시글 등록 완료!",
                    confirmButtonColor: "#A2DBA2DD"
                }).then((result) => {
                    if(result.value) {
                        location.href='/auth/qna';
                    }
                })
            },
            error: function() {
                Swal.fire({
                    icon: "error",
                    title: "게시글 등록 실패!",
                    text: "서버 오류가 발생했습니다.",
                    confirmButtonColor: "#ffcbad"
                });
            }
        });
    });
});