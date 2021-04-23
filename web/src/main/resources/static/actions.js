const attack_btn = $("form#attack .submit");
const attack_form = $("form#attack");
const move_form = $("form#move");
const move_btn = $("form#move .submit");
const soldier_form = $("form#soldier");
const soldier_btn = $("form#soldier .submit");
const tech_form = $("form#tech");
const tech_bin = $("form#tech .submit");
const buy_form = $("form#buy");
const buy_btn = $("form#buy .submit");
const move_spy_form = $("form#move_spy");
const move_spy_btn = $("form#move_spy .submit");
const res_cloaking_form = $("form#res_cloaking");
const res_cloaking_btn = $("form#res_cloaking .submit");
const spy_form = $("form#spy");
const spy_btn = $("form#spy .submit");
const tools_form = $("form#tools");
const tools_btn = $("form#tools .submit");

setupForm(attack_form, attack_btn, "/attack");
setupForm(move_form, move_btn, "/move");
setupForm(soldier_form, soldier_btn, "/soldier");
setupForm(tech_form, tech_bin, "/tech");
setupForm(buy_form, buy_btn, "/buy");
setupForm(move_spy_form, move_spy_btn, '/move_spy');
setupForm(res_cloaking_form, res_cloaking_btn, "/res_cloaking")
setupForm(spy_form, spy_btn, "/spy")
setupForm(tools_form, tools_btn, "/tools")

function setupForm(form, submit_btn, url) {
    form.submit(function (event) {
        event.preventDefault();
        submit_btn.toggleClass("disabled");
        const data = new FormData(event.target);
        const form_data = Object.fromEntries(data.entries());
        submit_action(url, form_data, submit_btn);
        form.trigger("reset");
        // hack to reset dropdown value
        $('.dropdown div.text').text(0)
    });
}

const submit_action = (url, form, submit_btn) => {
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: url,
        data: JSON.stringify(form),
        dataType: "json",
        success: function (resBody) {
            $("#msg_box")
                .empty()
                .append('<p style="user-select: auto;"> ' + resBody["valRes"] + "</p>");
            graphData = resBody["graphData"];
            // display map
            display_map(full_map_formatter);
            submit_btn.toggleClass("disabled");
        },
        error: () => {
            $("#msg_box")
                .append('<p style="user-select: auto;"> ' + " The action is invalid, please re-try with a valid one. " + "</p>");
            submit_btn.toggleClass("disabled");
        }
    });
};
