window.onload = function() {
    // 각 항목의 총 금액을 업데이트하는 함수
    function updateTotalPriceForItem(item) {
        var bookPrice = parseFloat(item.querySelector('.book_price').value);
        var quantity = parseInt(item.querySelector('input[name="amounts"]').value);
        var totalPrice = bookPrice * quantity;
        item.querySelector('#total_price').textContent = totalPrice.toLocaleString();
    }

    // 페이지 로드 시 각 장바구니 항목의 총 금액을 계산
    document.querySelectorAll('#total_list').forEach(function(item) {
        updateTotalPriceForItem(item);
    });

    // 감소 버튼 이벤트 리스너
    document.querySelectorAll('#minus').forEach(function(minusButton) {
        minusButton.addEventListener('click', function(event) {
            var quantityInput = minusButton.nextElementSibling; // 수량 input
            var currentQuantity = parseInt(quantityInput.value);
            if (currentQuantity > 1) { // 수량이 1보다 클 때만 감소
                quantityInput.value = currentQuantity - 1;
                updateTotalPriceForItem(minusButton.closest('#total_list'));
            }
        });
    });

    // 증가 버튼 이벤트 리스너
    document.querySelectorAll('#plus').forEach(function(plusButton) {
        plusButton.addEventListener('click', function(event) {
            var quantityInput = plusButton.previousElementSibling; // 수량 input
            var currentQuantity = parseInt(quantityInput.value);
            quantityInput.value = currentQuantity + 1;
            updateTotalPriceForItem(plusButton.closest('#total_list'));
        });
    });

    function checkItem(num) {
        if(num == 0) {
            location.href = "/noCart";
        }
    }

    // 장바구니 삭제 버튼 이벤트 리스너
    $('.box_button').click(function() {
        // 가장 가까운 total_list 요소를 찾아 이의 데이터를 가져옵니다.
        var item = $(this).closest('.total_list');
        var bookNum = item.find('.book_num').val();
        var userPrincipalValue = $('#userPrincipal').val();
        var userPrincipalId = $('#userPrincipal_id').val();
        console.log(item.length);

        // AJAX 요청을 통해 서버에 도서 ID와 함께 삭제 요청을 보냅니다.
        $.ajax({
            type: 'POST',
            url: '/auth/deleteBook', // 서버의 해당 도서 삭제 API URL
            data: JSON.stringify({
                book: {num: bookNum},
                member: {num: userPrincipalValue}
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function(response) {
                // 삭제 성공 시, UI에서 해당 항목 제거
                item.remove();
                console.log($('#total_wrap .total_list').length);
                // alert("도서가 장바구니에서 삭제되었습니다.");
                Swal.fire({
                    title: "삭제 성공",
                    text: "도서가 장바구니에서 삭제되었습니다.",
                    icon: "success",
                    confirmButtonColor: "#A2DBA2DD"
                }).then((result) => {
                    if(result.value) {
                        if ($('#total_wrap .total_list').length == 0) {
                            // 사용자 ID를 응답 데이터에서 추출하여 사용
                            var userId = response.data.userId; // 서버로부터 받은 사용자 ID
                            location.href = "/auth/qrMemberLogin?qrCode=" + userId + "&uid=-2"; // 사용자 ID를 쿼리 파라미터로 포함하여 리다이렉션
                        }
                    }
                })
            },
            error: function(xhr, status, error) {
                // 요청 실패 시, 오류 메시지 표시
                console.error(xhr.responseText); // 실패 시 에러 로그 출력
                console.log(status);
                // alert("해당 도서 삭제에 실패했습니다. 다시 시도해주세요.");
                Swal.fire({
                    title: "삭제 실패",
                    text: "해당 도서 삭제에 실패했습니다. 다시 시도해주세요.",
                    icon: "error",
                    confirmButtonColor: "#ffcbad"
                });
            }
        });
    });

    // 결제 모듈 초기화 및 결제 버튼 이벤트 리스너 설정
    const IMP = window.IMP;
    IMP.init("imp38665773");

    var memberId = $('#memberId').val();
    var memberName = $('#memberName').val();
    var memberEmail = $('#memberEmail').val();
    var memberPhone = $('#memberPhone').val();

    var today = new Date();
    var koreaTimeOffset = 9 * 60; // 한국은 UTC+9
    today.setMinutes(today.getMinutes() + today.getTimezoneOffset() + koreaTimeOffset);
    var year = today.getFullYear(); // 연도
    var month = today.getMonth() + 1; // 월 (0부터 시작하므로 +1)
    var day = today.getDate(); // 일
    var hours = today.getHours(); // 시
    var minutes = today.getMinutes(); // 분
    var seconds = today.getSeconds(); // 초
    var milliseconds = today.getMilliseconds();
    var makeMerchantUid = year.toString() + month.toString() + day.toString() +
        hours.toString() + minutes.toString() + seconds.toString() + milliseconds.toString();

    // YYYY-MM-DD HH:MM:SS 형식으로 변환
    var paid_at = `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} ${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;

    const button_inicis = document.getElementById('pay_button');

    button_inicis.addEventListener('click', async() => {
        console.log("click");

        let parameters = []; // 이 배열은 반복문 밖에서 선언되어야 합니다.

        let checkedBooks = []; // 선택된 도서들을 저장할 배열
        let totalAmount = 0; // 총 결제 금액
        let checkedBooksLength = 0; // 선택된 도서들의 개수

        document.querySelectorAll('.checkbox').forEach((checkbox, index) => {
            if (checkbox.checked) { // 체크된 체크박스만 처리
                const bookContainer = checkbox.closest('.total_list');
                const bookTitle = bookContainer.querySelector('.box_title').innerText;
                const bookPrice = parseInt(bookContainer.querySelector('.book_price').value);
                // const bookQuantity = parseInt(bookContainer.querySelector('.book_quantity').value);
                // const bookAmount = bookPrice * bookQuantity;
                const bookQuantity = parseInt(bookContainer.querySelector('input[name="amounts"]').value);
                const bookAmount = bookPrice * bookQuantity;

                totalAmount += bookAmount; // 총 금액 계산

                checkedBooks.push({
                    title: bookTitle,
                    price: bookPrice,
                    quantity: bookQuantity,
                    amount: bookAmount
                });
                console.log("bookQuantity: " + bookQuantity);
            }
        });
        checkedBooksLength = checkedBooks.length; // 모든 체크박스를 확인한 후, checkedBooks의 길이를 업데이트 합니다.
        console.log("checkedBooksLength: " + checkedBooksLength);
        var impName = '';
        if (checkedBooksLength > 0) { // checkedBooks 배열에 적어도 하나의 객체가 있는지 확인합니다.
            if (checkedBooksLength === 1) {
                impName = checkedBooks[0].title;
            } else {
                impName = checkedBooks[0].title + " 외 " + (checkedBooksLength - 1) + "권";
            }
        }

        // parameters.push는 모든 체크박스를 확인한 후 한 번만 실행되어야 합니다.
        if (checkedBooksLength > 0) { // checkedBooks 배열이 비어있지 않을 때만 실행합니다.
            parameters.push({
                checkedBooks: checkedBooks,
                totalAmount: totalAmount,
                checkedBooksLength: checkedBooksLength,
                memberId: memberId,
                memberName: memberName,
                memberEmail: memberEmail,
                memberPhone: memberPhone,
                impName: impName,
                paid_at: paid_at,
                makeMerchantUid: makeMerchantUid
            });
        }
        console.log("parameters.length: " + parameters.length);
        console.log("merchant_uid: " + parameters[0].makeMerchantUid);
        console.log("parameters.totalAmount: " + parameters[0].totalAmount);

        $.ajax({
            type: "POST",
            url: "/auth/cart/editBook",
            data: JSON.stringify({
                parameters: parameters[0]
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (response) {
                console.log("사전-장바구니 수정 성공:", response);
                $.ajax({
                    type: "POST",
                    url: "/auth/payments/prepare",
                    data: JSON.stringify({
                        merchant_uid: "IMP" + parameters[0].makeMerchantUid,
                        parameters: parameters[0]
                    }),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function (response) {
                        // API 호출 성공 시, 실행될 코드
                        inicis_pay(parameters[0]);
                    },
                    error: function (xhr, status, error) {
                        // API 호출 실패 시, 실행될 코드
                        console.error("결제 준비 실패:", error);
                    }
                });
            },
            error: function (xhr, status, error) {
                console.error("사전-장바구니 수정 실패:", error);
            }
        });

    });

    function inicis_pay(parameters) {
        IMP.request_pay({
            pg: 'html5_inicis',
            merchant_uid: "IMP" + parameters.makeMerchantUid,
            name: parameters.impName,
            amount: parameters.totalAmount,
            buyer_name: parameters.memberName,
            buyer_tel: parameters.memberPhone,
            buyer_email: parameters.memberEmail,
            paid_at: parameters.paid_at
        }, function (response) {
            if (response.success) {
                console.log(response);
                // 객체 자체를 콘솔에 출력
                console.log("parameters:", parameters);
                // 객체를 이쁘게(예쁘게) 출력
                console.log("Formatted parameters:", JSON.stringify(parameters, null, 2));
                sendAjaxRequest(parameters);
                // alert("결제 성공");
                Swal.fire({
                    title: "결제 성공",
                    icon: "success",
                    confirmButtonColor: "#A2DBA2DD"
                }).then((result) => {
                    if (result.isConfirmed) {
                        location.href = "/auth/payment/complete?userId=" + parameters.memberId;
                    }
                });
            } else {
                console.error(response);
                // alert("결제 실패");
                Swal.fire({
                    title: "결제 실패",
                    icon: "error",
                    confirmButtonColor: "#ffcbad"
                });
            }
        });
    }

    function sendAjaxRequest(parameters) {
        // 객체 자체를 콘솔에 출력
        console.log("parameters.checkedBooks:", parameters.checkedBooks.toString());
        // 객체를 이쁘게(예쁘게) 출력
        console.log("Formatted parameters:", JSON.stringify(parameters, null, 2));
        $.ajax({
            type: "POST",
            url: "/auth/payments/after",
            data: JSON.stringify({
                parameters
            }),
            contentType: "application/json",
            success: function(response) {
                console.log("서버로부터의 응답:", response);
                // 응답 처리 로직
            },
            error: function(xhr, status, error) {
                console.error("AJAX 요청 실패:", error);
                // 오류 처리 로직
            }
        });
    }

};