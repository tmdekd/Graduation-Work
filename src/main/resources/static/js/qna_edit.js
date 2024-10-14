$(document).ready(function() {
    $('#write_button').click(function() {
        var questionId = $(this).data('id');

        $.ajax({
            type: 'PUT',
            url: '/qna/question/edit/'+ questionId,
            contentType: 'application/json',
            data: JSON.stringify({
                title: $('#question_input').val(), // 제목 입력 필드의 ID가 question_input이므로 이를 사용
                content: $('#question_textarea').val(),}),
            success: function(response) {
                if (response.status === 200 || response.status === 201) {
                    Swal.fire({
                        icon: "success",
                        title: "게시글 수정 완료!",
                        text: "게시글이 수정되었습니다.",
                        confirmButtonColor: "#A2DBA2DD"
                    }).then((result) => {
                        if(result.value) {
                            location.href = '/auth/qna_detail/'+ questionId;
                        }
                    })
                } else {
                    Swal.fire({
                        icon: "error",
                        title: "게시글 수정 실패!",
                        text: "저장 중 오류가 발생했습니다.",
                        confirmButtonColor: "#ffcbad"
                    });
                }
            },
            error: function() {
                Swal.fire({
                    icon: "error",
                    title: "게시글 수정 실패!",
                    text: "저장 중 오류가 발생했습니다.",
                    confirmButtonColor: "#ffcbad"
                });
            }
        });
    });
});