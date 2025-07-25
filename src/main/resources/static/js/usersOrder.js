$(function () {

    let path = window.location.pathname;
    if(path.includes("/roomservice/order/main")){
        showMenuList();
    }






    function showMenuList() {
        $.ajax({

            url: "/roomService/order/showMainList",
            type: "GET",
            success: function (result) {
                menuList(result)
            }

        })

    }


    function menuList(menus) {
        $(".menuListWrap").empty();

        let groupedMenus = {};

        menus.forEach(function (menu) {
            let storeName = menu.storeDTO.name;
            if(!groupedMenus[storeName]){
                groupedMenus[storeName] = [];
            }
            groupedMenus[storeName].push(menu);

        })
        Object.keys(groupedMenus).forEach(function (storeName){
            let storeSection = $('<div class = "storeSection"></div>');
            storeSection.append('<h3>'+storeName+'</h3>');
            let itemList = $('<ul class = "storeItemList"></ul>');

            groupedMenus[storeName].forEach(function (menu){
                let menuItem = '<li data-menunum="' + menus.id + '" >' +
                    '<span class="menuName">' + menu.name + '</span>' +
                    '<span class="menuContent">' + menu.content + '</span>' +
                    '<button type="button" data-menunum="' + menu.id + '" class="cartAdd">장바구니 담기</button>' +
                    '</li>';
                itemList.append(menuItem);
            })
            storeSection.append(itemList);
            $(".menuListWrap").append(storeSection); // 메인 리스트에 추가
        })

    }

})