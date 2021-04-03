const attack_btn = $('form#attack .submit');
const attack_form = $('form#attack');
const move_form = $('form#move');
const move_btn = $('form#move .submit');

setupForm(attack_form, attack_btn, '/attack');
setupForm(move_form, move_btn, '/move');

function setupForm(form, submit_btn, url) {
    form.submit(function (event) {
        event.preventDefault();
        submit_btn.toggleClass("disabled");
        const data = new FormData(event.target);
        const form_data = Object.fromEntries(data.entries());
        submit_action(url, form_data, submit_btn);
        form.trigger("reset");
    })
}

const submit_action = (url, form, submit_btn) => {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: url,
        data: JSON.stringify(form),
        dataType: 'json',
        success: function (resBody) {
            $('#msg_box').append('<p style="user-select: auto;"> ' + resBody['valRes'] + '</p>');
            graphData = resBody['graphData'];
            // display map
            display_map(full_map_formatter)
            submit_btn.toggleClass("disabled");
        }
    });
}