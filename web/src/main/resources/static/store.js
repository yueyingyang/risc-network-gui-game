// This file are functions and constant related to store modal display

display_store_modal = () => {
    $('#store').modal('show');
}
hide_buy_modal = () => {
    $('#store').modal('hide');
}
// product list
products = [
    {
        "name": "Missile",
        "price": 100,
        "desc": "Clear all soldiers on target, boom!",
        "path_to_img": "missile.png",
    },
    {
        "name": "Ship",
        "price": 50,
        "desc": "Cross the ocean then you don't a path",
        "path_to_img": "ship.png"
    },
    {
        "name": "Shield",
        "price": 50,
        "desc": "Safer guard to your territory",
        "path_to_img": "shield.png"
    },
    {
        "name": "Sword",
        "price": 50,
        "desc": "Attack with more power",
        "path_to_img": "sword.png"
    }
]

// Render product list
let list_html = ""
$.each(products, function (key, value) {
    list_html += '<div class="item" style="user-select: auto;">\n' +
        '<img src="/images/' + value.path_to_img + '" class="ui inline image"\n' +
        'style="width: 40px;">\n' +
        '<div class="content" style="user-select: auto;">\n' +
        '<div class="header">' + value.name +
        '  <a class="ui tag label tiny" style="user-select: auto;">\n' + value.price +
        '  </a></div>\n' +
        '<div class="description">' + value.desc + '</div>' +
        '</div>\n' +
        '</div>';
});
$('#prop_list').append(list_html);

// Get a mapping from product name to product price
const product_price = {}
products.forEach(item => {
    product_price[item.name.toLowerCase()] = parseInt(item.price)
})
// Calculate total price
const update_total = () => {
    const type = $("form#buy [name*='fromType']").val().toLowerCase()
    const amount = parseInt($("form#buy [name*='soldierNum']").val())
    let total = isNaN(product_price[type] * amount) ? 0 : product_price[type] * amount;
    $("#total").empty().append(total)
}
