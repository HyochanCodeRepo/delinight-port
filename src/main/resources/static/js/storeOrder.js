$(function(){

    showProcessingList();
    showCompleteList();

    function showProcessingList(){
        var str = null;

        $(".trWrap").empty()
        $.ajax({

            url:"/store/orders/processingList",
            type:"get",
            success:function (result) {
                console.log(result)
                result.forEach(function (order) {
                    if(order!==null){
                    const itemsJson =encodeURIComponent(JSON.stringify(order.ordersItemDTOList))
                    str += "<tr><td scope='col'>"+order.id+"</td><td scope='col'>"+order.checkInDTO.roomDTO.hotelDTO.name+"</td><td scope='col'>"+order.checkInDTO.roomDTO.name+"</td><td>"+formatLocalDateTime(order.awaitingTime)+"</td><td scope='col'>"+(order.preparingTime ? formatLocalDateTime(order.preparingTime):"-")+"</td><td scope='col'>"+(order.deliveringTime ? formatLocalDateTime(order.deliveringTime):"-")+"</td><td scope='col'><button type='button' class='btn btn-outline-secondary memoCheck' data-memo='"+order.memo+"'>확인하기</button></td><td scope='col'><button type='button' class='btn btn-outline-secondary menuCheck' data-orderitemdtos='"+itemsJson+"'>확인하기</button></td><td scope='col'>"+checkStatus(order.ordersStatus)+"</td><td>"+checkStatusChangeButton(order.ordersStatus,order.id)+"</td> </tr>"
                    }
                })
                $(".trWrap").html(str)

            },
            fail:function (result) {
                alert(result)
            },


        })

    }

    $(document).on("click", ".memoCheck", function () {

        let memoIsNull = "요청 사항이 없습니다."
        let memo = $(this).data("memo")
        Swal.fire({
            title: '요청사항',
            html: '<div style="height: 200px;display: flex;justify-content: center;align-items: center;">'+(memo!==""?memo:memoIsNull)+'</div>',
            width: '650px',

        });
    })

    $(document).on("click", ".menuCheck", function () {
        const raw = $(this).attr("data-orderitemdtos");
        const ordersItemDTOList = JSON.parse(decodeURIComponent(raw)); // 아까 한 인코딩 다시 디코딩

        let total = 0; // 총 가격 계산용
        let rows = ordersItemDTOList.map((item, index) => {
            const price = item.menuDTO.price;
            const quantity = item.quantity;
            const subtotal = price * quantity;
            total += subtotal;

            return `
            <tr>
                <td style="text-align: center">${index + 1}</td>
                <td>${item.menuDTO.name}</td>
                <td style="text-align: center">${quantity}</td>
                <td class="text-end">${price.toLocaleString()}원</td> 
            </tr>
        `; // toLocaleString은 천단위로 콤마찍는거
        }).join("");

        const tableHtml = `
        <div style="overflow-x:auto; padding: 2em;">
            <table class="table table-bordered table-sm text-start" style="margin-bottom: 1em;">
                <thead class="table-light">
                    <tr style="text-align: center">
                        <th scope="col">순번</th>
                        <th scope="col">메뉴 이름</th>
                        <th scope="col">수량</th>
                        <th scope="col" class="text-center">가격</th>
                    </tr>
                </thead>
                <tbody>
                    ${rows}
                </tbody>
            </table>
            <div class="fw-bold text-end" style="padding: 0.5em 0; font-size: 1.1em;">
                총 가격 : ${total.toLocaleString()}원
            </div>
        </div>
    `;


        Swal.fire({
            title: '주문한 메뉴',
            html: tableHtml,
            width: '650px',

        });
    });

    function formatLocalDateTime(dateTimeStr) {
        if (!dateTimeStr) return "-";

        const date = new Date(dateTimeStr);
        const yyyy = String(date.getFullYear());
        const MM = String(date.getMonth() + 1).padStart(2, '0'); // 월 (0-based)
        const dd = String(date.getDate()).padStart(2, '0');
        const hh = String(date.getHours()).padStart(2, '0');
        const mm = String(date.getMinutes()).padStart(2, '0');
        const ss = String(date.getSeconds()).padStart(2, '0');

        return `${yyyy}-${MM}-${dd} ${hh}:${mm}:${ss}`;
    }
    function checkStatus(status){
        switch (status){
            case "AWAITING" :
                return "승인 대기중"
            case "PREPARING" :
                return "조리 중"
            case "DELIVERING" :
                return "배달 중"
            case "DELIVERED" :
                return "배달 완료"
        }
    }
    function checkStatusChangeButton(status, id){
        switch (status){
            case "AWAITING" :
                return "<button class='btn btn-outline-primary changeBtn'  data-current='awaiting' data-orderid='"+id+"'>승인하기</button>"
            case "PREPARING" :
                return "<button class='btn btn-outline-success changeBtn' data-current='preparing' data-orderid='"+id+"'>배달시작</button>"
            case "DELIVERING" :
                return "<button class='btn btn-outline-success changeBtn' data-current='delivering' data-orderid='"+id+"'>배달완료</button>"
            case "DELIVERED" :
                return "배달 완료"
        }
    }

    $(document).on("click",".changeBtn", function () {
        let current = $(this).data("current")
        let ordersId = $(this).data("orderid")
        statusChange(current, ordersId)
        showCompleteList()

    })

    function statusChange(response, ordersId){
        switch (response){
            case "awaiting" :
                return changeAjax("preparing", ordersId)
            case "preparing" :
                return changeAjax("delivering", ordersId)
            case "delivering" :
                return changeAjax("delivered", ordersId)

        }

    }
    async function changeAjax(response, ordersId) {
        $.ajax({
            url:"/store/orders/changeStatus",
            type:"post",
            data:{"ordersId":ordersId,"ordersStatus":response},
            success:function () {
                showProcessingList();
            },
            error:function () {
                alert("오류")
            }
        })
    }

    function showCompleteList(){
        var str = null;

        $(".trWrap2").empty()
        $.ajax({

            url:"/store/orders/completeList",
            type:"get",
            success:function (result) {
                console.log(result)
                result.forEach(function (order) {
                    if (order !== null) {

                    const itemsJson = encodeURIComponent(JSON.stringify(order.ordersItemDTOList))
                    str += "<tr><td scope='col'>" + order.id + "</td><td scope='col'>" + order.checkInDTO.roomDTO.hotelDTO.name + "</td><td scope='col'>" + order.checkInDTO.roomDTO.name + "</td><td>" + formatLocalDateTime(order.awaitingTime) + "</td><td scope='col'>" + (order.preparingTime ? formatLocalDateTime(order.preparingTime) : "-") + "</td><td scope='col'>" + (order.deliveringTime ? formatLocalDateTime(order.deliveringTime) : "-") + "</td><td scope='col'>" + (order.deliveringTime ? formatLocalDateTime(order.deliveredTime) : "-") + "</td><td scope='col'><button type='button' class='btn btn-outline-secondary memoCheck' data-memo='" + order.memo + "'>확인하기</button></td><td scope='col'><button type='button' class='btn btn-outline-secondary menuCheck' data-orderitemdtos='" + itemsJson + "'>확인하기</button></td> </tr>"
                }
                })
                $(".trWrap2").html(str)

            },
            fail:function (result) {
                alert(result)
            },


        })

    }



})