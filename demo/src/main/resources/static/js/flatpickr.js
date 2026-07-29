document.addEventListener('DOMContentLoaded', function () {
    const target = document.querySelector('#fromCheckinDateToCheckoutDate');

    // 未ログイン時など、要素が存在しないページでは何もしない
    if (!target) {
        return;
    }

    const maxDate = new Date();
    maxDate.setMonth(maxDate.getMonth() + 3);

    flatpickr(target, {
        mode: 'range',
        locale: 'ja',
        minDate: 'today',
        maxDate: maxDate,
        dateFormat: 'Y-m-d',
        allowInput: true
    });
});