document.addEventListener('DOMContentLoaded', function() {
    // URL에서 쿼리 파라미터 값을 읽어오는 함수
    function getQueryParamValue(key) {
        const queryParams = new URLSearchParams(window.location.search);
        return queryParams.get(key);
    }

    // 'uid' 쿼리 파라미터의 값을 얻기
    const uid = getQueryParamValue('uid');
    console.log(uid);

    // 얻은 'uid' 값으로 페이지 이동
    if (uid) {
        window.location.href = `/auth/scan_noCategory?uid=${uid}`;
    } else {
        console.error('UID 값이 URL에 없습니다.');
    }
});