$(function () {
    $(".userHeaderMsg").html("장바구니").css("font-weight","bold")

    showList();


    function showList() {
        $.ajax({

            url: "/cart/showList",
            type: "GET",
            success: function (result) {
                cartList(result)
            }

        })

    }

    $(document).on("click", ".plusQuantity", function () {


        let cartItemNum = $(this).data("cartitemnum")
        $.ajax({

            url: "/cart/plusQuantity",
            type: "post",
            data: {"cartItemNum": cartItemNum},
            success: function (result) {
                showList()
            },
            error: function () {
                alert("실패")
            }
        })
    })
    $(document).on("click", ".minusQuantity", function () {


        let cartItemNum = $(this).data("cartitemnum")
        $.ajax({

            url: "/cart/minusQuantity",
            type: "post",
            data: {"cartItemNum": cartItemNum},
            success: function (result) {
                showList()
            },
            error: function () {
                alert("실패")
            }
        })
    })

    function cartList(carts) {
        $(".storeReceipt").empty();
        $(".totalPrice").empty();
        $(".cartList").empty();
        let totalPrice = 0;
        let groupedCarts = {};
        if (carts.length === 0) {
            // 카트가 비어있으면 문구 표시
            $(".cartList").append('<div style="text-align: center; margin-top: 20px; font-size: 1.2em; color: gray;">현재 장바구니가 비어있습니다</div>');
            return; // 더 이상 진행할 필요 없음
        }


        carts.forEach(function (cart) {
            let storeName = cart.menuDTO.storeDTO.name;
            if (!groupedCarts[storeName]) {
                groupedCarts[storeName] = [];
            }
            groupedCarts[storeName].push(cart);
            totalPrice += (cart.menuDTO.price) * (cart.quantity)

        })
        Object.keys(groupedCarts).forEach(function (storeName) {
            let storeSection = $('<div class = "storeSection"></div>');
            storeSection.append('<p style="font-size: 1.2em; font-weight: bold; margin-bottom: 10px">' + storeName + '</p>');
            let itemList = $('<div class = "row itemsWrap"></div>');
            let storePrice = 0;

            groupedCarts[storeName].forEach(function (cart) {

                let priceFormatted = cart.menuDTO.price.toLocaleString('ko-KR') + '원';

                let cartItem = '<div class="itemWrap row"><div class="col-4">' +
                    '<div style="width: 100px; height: 100px;"><img class="imgBox" src="'+cart.menuDTO.imgUrl+'"></div></div><div class="col-5 itemContents"><div class="row itemName">'+cart.menuDTO.name+'</div><div class="row itemContent">'+cart.menuDTO.content+'</div><div class="row itemPrice">'+priceFormatted+'</div></div>' +
                    '<div class="col-3" style="display: flex; align-items: center;"><span class="menuQuantity" style="display: flex; align-items: centerl" >' +
                    '<img src="/img/dash-circle.svg" class="minusQuantity" data-cartitemnum="'+cart.id+'" style="height: 1.5em; width: 1em;">&nbsp' + cart.quantity +
                    '&nbsp<img src="/img/plus-circle.svg" class="plusQuantity" data-cartitemnum="'+cart.id+'" style="height: 1.5em; width: 1em;">&nbsp<img src="/img/bag-x.svg" data-cartitemnum="' + cart.id + '" class="cartItemDelete" style="height: 1.5em; width: 1em;"> ' +
                    '</span>' +
                    '</div></div>';
                itemList.append(cartItem);
                storePrice += cart.menuDTO.price*cart.quantity;


            })
            storeSection.append(itemList);
            let storePriceFormatted =storePrice.toLocaleString('ko-KR') + '원';

                $(".storeReceipt").append('<p style="width: 95%; display: flex; justify-content: space-between;"><span>'+storeName+'</span><span>'+storePriceFormatted+'</span></p>')
            $(".cartList").append(storeSection); // 메인 리스트에 추가
        })
        let totalPriceFormatted =totalPrice.toLocaleString('ko-KR') + '원';

        $(".storeReceipt").append('<p style="margin-top:10px; margin-bottom:10px;width: 95%; display: flex; justify-content: space-between;"><span>총 결제 금액</span><span>'+totalPriceFormatted+'</span></p>')

    }

    $(document).on("click", ".cartAdd", function () {

        let selectedMenu = $(this).data("menunum")
        $.ajax({
            url: "/cart/addToCart",
            type: "POST",
            data: {"menuNum": selectedMenu},
            success: function (result) {


            }

        })

    })
    $(".clearBtn").on("click", function () {

        $.ajax({
            url: "/cart/clear",
            type: "POST",
            success: function () {
                showList()
            }


        })

    })

    $(document).on("click", ".cartItemDelete", function () {


        let selecetedCartItem = $(this).data("cartitemnum")
        $.ajax({
            url: "/cart/removeFromCart",
            type: "POST",
            data: {"cartItemNum": selecetedCartItem},
            success: function (result) {
                showList();
            }

        })
    })

    $(".orderBtn").on("click", function () {

        $.ajax({

            url: "/cart/cartToOrder",
            type: "post",
            success: function (result) {
                window.location.href = '/users/order/request?paymentId='+result;
            }

        })

    })
})