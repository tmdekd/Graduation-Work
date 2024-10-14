$(document).ready(function() {
    // 찜 목록 삭제 버튼 이벤트 리스너
    $('.box_button').click(function() {
        // 가장 가까운 book_list 요소를 찾아 이의 데이터를 가져옵니다.
        var item = $(this).closest('.total_heartfull_list');
        var wishlistId = item.find('.wishlist_id').val();  // 찜 목록의 ID를 가져옵니다.

        // AJAX 요청을 통해 서버에 찜 목록 항목 ID와 함께 삭제 요청을 보냅니다.
        $.ajax({
            type: 'POST',
            url: '/tweetBook/delete', // 서버의 해당 찜 목록 항목 삭제 API URL
            data: JSON.stringify({
                id: wishlistId
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(response) {
                // 삭제 성공 시, UI에서 해당 항목 제거
                item.remove();
                // alert("찜 목록에서 해당 도서가 삭제되었습니다.");
                // Swal.fire({
                //     icon: "success",
                //     title: "회원가입이 완료되었습니다."
                // }).then((result) => {
                //     if(result.value) {
                //         location.href = "/auth/login_joinForm";
                //     }
                // })
                Swal.fire({
                    title: "삭제 성공",
                    text: "찜 목록에서 해당 도서가 삭제되었습니다.",
                    icon: "success",
                    confirmButtonColor: "#A2DBA2DD"
                }).then((result) => {
                    if ($('.wishlist .book_list').length == 0) {
                        location.href = "/tweetBook";
                    }
                })
            },
            error: function(xhr, status, error) {
                // 요청 실패 시, 오류 메시지 표시
                console.error(xhr.responseText); // 실패 시 에러 로그 출력
                // alert("찜 목록에서 해당 도서 삭제에 실패했습니다. 다시 시도해주세요.");
                Swal.fire({
                    title: "삭제 실패",
                    html: "찜 목록에서 해당 도서 삭제에 실패했습니다. <br>다시 시도해주세요.",
                    icon: "error",
                    confirmButtonColor: "#ffcbad"
                });
            }
        });
    });
});