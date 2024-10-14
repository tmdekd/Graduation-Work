document.addEventListener('DOMContentLoaded', function() {
    let toggleBtn = document.getElementById('icon');
    let menu = document.querySelector('.navbar_menu');
    console.log(menu);
    let login = document.querySelector('.login_button');
    console.log(login);
    let cart = document.querySelector('.class_cart');

    toggleBtn.addEventListener('click', () => {
        menu.classList.toggle('active');
        login.classList.toggle('active');
        cart.classList.toggle('active');

    });
});



$(document).ready(function() {
    var originalNavMenuHeight = $('#nav_menu').height(); // 원래 높이 저장
    $('li').hover(
        function(event) { // 마우스가 요소 위로 올라갔을 때
            event.preventDefault();
            var navbarMenuUlHeight = $('.navbar_menu_ul').outerHeight(true); // navbar_menu_ul의 총 높이(패딩, 마진 포함) 계산
            $('#nav_menu').css('padding-bottom', navbarMenuUlHeight + 'px'); // nav_menu의 패딩 또는 마진을 조정하여 공간 확보
            $('.navbar_menu_ul').css('display', 'block');
        },
        function(event) { // 마우스가 요소에서 벗어났을 때
            event.preventDefault();
            $('#nav_menu').css('padding-bottom', '0'); // 원래 상태로 복구
            $('.navbar_menu_ul').css('display', 'none');
        }
    );
});


