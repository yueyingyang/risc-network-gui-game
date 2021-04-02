// Enable select
$('select.dropdown')
    .dropdown();

// Set interaction of action tabs
$('#actions a.item').on('click', function () {
    $(this)
        .addClass('active')
        .siblings()
        .removeClass('active');
})