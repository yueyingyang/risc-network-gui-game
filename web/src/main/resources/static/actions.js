const attack_btn = $("form#attack .submit");
const attack_form = $("form#attack");
const move_form = $("form#move");
const move_btn = $("form#move .submit");
const soldier_form = $("form#soldier");
const soldier_btn = $("form#soldier .submit");
const tech_form = $("form#tech");
const tech_btn = $("form#tech .submit");
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
setupForm(tech_form, tech_btn, "/tech");
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

function action_str(form, btn) {
    switch (btn) {
        case buy_btn:
            return "BUY " + form['soldierNum'] + " " + form["fromType"].toLowerCase();
        case tools_btn:
            return "use TOOL " + form['fromType'] + " on " + form['toName']
        case attack_btn:
            return "ATTACK territory" + form['toName'] + " from " + form['fromName'] + " using" + '💂' + "(x" + form["soldierNum"] + ' of type ' + form['fromType'] + ")"
        case move_btn:
            return "MOVE " + '💂' + "(x" + form["soldierNum"] + ' of type ' + form['fromType'] + ")" + " from " + form['fromName'] + " to " + form['toName']
        case move_spy_btn:
            return "move 👀(x" + form['soldierNum'] + ")" + "to " + form['toName']
        case soldier_btn:
            return "upgrade 💂 from " + form['fromType'] + " to " + form['toType'] + "(on territory" + form['fromName'] + ")"
        case spy_btn:
            return "upgrade a 👀 " + "on territory" + form['fromName'];
        case tech_btn:
            return "Upgrade TECH level by 1"
        case res_cloaking_btn:
            return "Research CLOAKING"
        default:
            return ""
    }
}

// submit form to url
const submit_action = (url, form, submit_btn) => {
    let action_des = "[📩] " + action_str(form, submit_btn) + "?"
    $("#msg_box")
        .empty()
        .append(action_des)
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: url,
        data: JSON.stringify(form),
        dataType: "json",
        success: function (resBody) {
            $("#msg_box")
                .empty()
                .append(action_des)
                .append('<p style="user-select: auto;"> ' + resBody["valRes"] + "</p>");
            graphData = resBody["graphData"];
            // display map
            display_map(full_map_formatter);
            submit_btn.toggleClass("disabled");
        },
        error: function () {
            $("#msg_box")
                .append('<p style="user-select: auto;"> ' + " The action is invalid, please re-try with a valid one. " + "</p>");
            $("#commit").removeClass("disabled");
            $("div.field").removeClass("disabled");
            $(".submit.button.basic").removeClass("disabled");
        }
    });
};
