var interval = function() {
    setTimeout(function() {

        $.ajax({
            url:"getAllAccounts.php",
            // Ajax options goes here
            success : function(data) {
                var jsondata=$.parseJSON(data);
                $("#item_table").find("tr:gt(0)").remove();
                $.each(jsondata, function(i, it) {
                    
                    $('<tr>').append(
                        $('<td>').text(it.name),
                        $('<td>').text(it.account_id),
                        $('<td>').text('\u20AC '+it.balance)
                    ).appendTo('#item_table');
                    // $('#records_table').append($tr);
                    //console.log($tr.wrap('<p>').html());
                });
                // Process Data
                // Generate HTML
                // Insert Generated HTML whereever you want in document
                interval() //Initiate next fetch
            }
        })

    }, 1000 // Interval time
    );
};

interval();