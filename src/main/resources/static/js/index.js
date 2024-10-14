window.addEventListener('DOMContentLoaded', (event) => {
    document.getElementById('submit_icon').addEventListener('click', function() {
        var searchInput = document.getElementById('search').value;
        if(searchInput.trim() === '') {
            // alert('입력한 키워드가 없습니다.');
            Swal.fire({
                title: "입력한 키워드가 없습니다.",
                icon: "error",
                confirmButtonColor: "#ffcbad"
            });
        } else {
            document.getElementById('id_div2_form').submit();
        }
    });

    document.getElementById('top_btn_icon').addEventListener('click', function(event) {
        event.preventDefault();
        window.scroll({
            top: 0,
            left: 0,
            behavior: 'smooth'
        });
    });

    document.getElementById('search').value = '';
});