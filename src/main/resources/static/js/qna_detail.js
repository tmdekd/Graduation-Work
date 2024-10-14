$(document).ready(function() {
    $('#rewrite').click(function() {
        var questionId = $(this).data('id');
        window.location.href = '/qna/question/edit/' + questionId;
    });
});
$(document).ready(function() {
    $('#delete').click(function() {
        var questionId = $(this).data('id');
        Swal.fire({
            title: "정말 삭제하시겠습니까?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Delete"
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    type: 'DELETE',
                    url: '/qna/question/delete/' + questionId,
                    success: function(response) {
                        if (response.status === 200) {
                            Swal.fire({
                                icon: "success",
                                title: "게시글 삭제 완료",
                                text: "게시글이 삭제되었습니다.",
                                confirmButtonColor: "#A2DBA2DD"
                            }).then((result) => {
                                if(result.value) {
                                    location.href = "/auth/qna";
                                }
                            })
                        } else {
                            Swal.fire({
                                icon: "error",
                                title: "게시글 삭제 실패!",
                                text: "다시 시도해 주세요",
                                confirmButtonColor: "#ffcbad"
                            });
                        }
                    },
                    error: function() {
                        Swal.fire({
                            icon: "error",
                            title: "게시글 삭제 실패!",
                            text: "다시 시도해 주세요",
                            confirmButtonColor: "#ffcbad"
                        });
                    }
                });

            }

        });
    });
});

$(document).ready(function(){
    $('#comment_button').click(function(){
        var questionId = $(this).data('id');
        console.log($('#answer_input').val())

        $.ajax({
            url: '/qna/qna_comment', // 게시글을 등록하는 서버의 URL
            type: 'POST',
            contentType: 'application/json', // JSON 형식의 데이터를 서버로 전송하기 위한 설정
            data: JSON.stringify({
                content: $('#answer_input').val(),
                question: { id: questionId } // 질문 ID 전달
            }),

            success: function(response) {
                if (response.status === 200) {
                    location.href='/auth/qna_detail/' + questionId;
                } else {
                    Swal.fire({
                        icon: "error",
                        title: "댓글 등록 실패!",
                        text: "다시 시도해 주세요",
                        confirmButtonColor: "#ffcbad"
                    });
                }
            },
            error: function() {
                Swal.fire({
                    icon: "error",
                    title: "댓글 등록 실패!",
                    text: "다시 시도해 주세요",
                    confirmButtonColor: "#ffcbad"
                });
            }
        });
    });
});

//댓글 삭제
$(document).ready(function() {
    $(document).on('click', '.admin_delete', function() {
        //$('.admin_delete').click(function() {
        // var commentId = $(this).data('id');
        var commentId = $(this).data('id'); // 수정된 부분
        if (!commentId) {
            console.log("삭제할 댓글의 ID가 정의되지 않았습니다.");
            return;
        }
        var $commentElement = $(this).closest('.comment');
        console.log("삭제할 댓글의 ID:", commentId);

        Swal.fire({
            title: "정말 삭제하시겠습니까?",
            icon: "warning",
            showCancelButton: true,
            confirmButtonColor: "#3085d6",
            cancelButtonColor: "#d33",
            confirmButtonText: "Delete"
        }).then((result) => {
            if (result.isConfirmed) {
                $.ajax({
                    type: 'DELETE',
                    url: '/qna/comment/delete/' + commentId,
                    success: function(response) {
                        if (response.status === 200) {
                            // alert('삭제가 완료되었습니다.');
                            Swal.fire({
                                icon: "success",
                                title: "댓글 삭제 완료",
                                html: "삭제되었습니다.",
                                confirmButtonColor: "#A2DBA2DD"
                            }).then((result) => {
                                if(result.value) {
                                    $commentElement.remove();
                                    location.reload();
                                }
                            })

                        } else {
                            Swal.fire({
                                icon: "error",
                                title: "댓글 삭제 실패!",
                                text: "다시 시도해 주세요",
                                confirmButtonColor: "#ffcbad"
                            });
                        }
                    },
                    error: function() {
                        Swal.fire({
                            icon: "error",
                            title: "댓글 삭제 실패!",
                            text: "다시 시도해 주세요",
                            confirmButtonColor: "#ffcbad"
                        });
                    }
                });
            }
        });
    });
});

