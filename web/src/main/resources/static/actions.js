const attack_btn = $('form#attack .submit');
const attack_form = $('form#attack');

setupForm(attack_form, attack_btn, '/attack');

function setupForm(form, submit_btn, url) {
    form.submit(function (event) {
        event.preventDefault();
        submit_btn.toggleClass("disabled");
        const data = new FormData(event.target);
        const form_data = Object.fromEntries(data.entries());
        submit_action(url, form_data);
        // ugly and rude way to clear the form
        $(':input')
            .not(':button, :submit, :reset, :hidden')
            .val('')
            .removeAttr('checked')
            .removeAttr('selected');
    })
}

const submit_action = (url, form) => {
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
            attack_btn.toggleClass("disabled");
        }
    });
}