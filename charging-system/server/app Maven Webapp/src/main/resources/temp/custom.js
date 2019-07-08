/**
 * Turn form to json.
 * @param formId Must be form element.
 */
function custom_formToJson(formId) {
    var formSerializeDataArray = $("#" + formId).serializeArray();
    var pairs = "";
    var json = "";
    for (i in formSerializeDataArray) {
        var name = formSerializeDataArray[i].name;
        var value = formSerializeDataArray[i].value;
        if (value.trim() != "") {
            var pair = '"' + name + '":' + '"' + value + '"';
            pairs += ',' + pair;
        }
    }
    json += '{' + pairs.substring(1) + '}';
    return json;
}

/**
 * Generate a uuid value.
 */
function custom_generateUUID() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random() * 16) % 16 | 0;
        d = Math.floor(d / 16);
        return (c == 'x' ? r : (r & 0x7 | 0x8)).toString(16);
    });
    return uuid;
}

/**
 * Sift value from given table.
 * Must be run after document was ready.
 * 
 * @param input_valueId Must be input element
 * @param select_modeId Must be select element
 * @param select_id Must be select element
 * @param table_id Must be table element
 */
function custom_siftBasedOnColumn(input_valueId, select_modeId, select_id, table_id) {
    //add options to select element
    if ($("#" + select_id).find("option").length < 1) {
        var options = "";
        var tds = $("#" + table_id).find("thead").find("td");
        var i = 0;
        $(tds).each(function() {
            options += '<option value="' + i + '">' + $(this).text() + "</option>";
            i++;
        })
        $("#" + select_id).html(options);
    }
    //implements of sift service
    if ($("#" + input_valueId).attr("onkeydown") != null) {
        var mode = $("#" + select_modeId + " option:selected").val();
        var colName = $("#" + select_id + " option:selected").val();
        var value = $("#" + input_valueId).val();
        var tds = $("#" + table_id).find("tbody").find("tr").find("td:eq(" + colName + ")");
        $.each(tds, function() {
            if (mode == 0) {
                if ($(this).text().indexOf(value) >= 0) {
                    $(this).parent("tr").show();
                } else {
                    $(this).parent("tr").hide();
                }
            } else if (mode == 1) {
                if ($(this).text().indexOf(value) < 0) {
                    $(this).parent("tr").hide();
                }
            }
        });
    }
    //bind event to input element
    if ($("#" + input_valueId).attr("onkeydown") == null) {
        $("#" + input_valueId).attr("onkeydown", "if(event.keyCode==13){event.keyCode=0;event.returnValue=false;custom_siftBasedOnColumn('" + input_valueId + "','" + select_modeId + "','" + select_id + "','" + table_id + "');}")
    }
}

/**
 * Sort table when thead was clicked.
 * 
 * @param table_id Must be table element
 */
function custom_sortOnTheadClick(table_id) {
    var tds_thead = $("#" + table_id).find("thead").find("td");
    var i = 0;
    $(tds_thead).each(function() {
        if ($(this).attr("onclick") == null) {
            $(this).attr("onclick", "custom_sortOnTheadClick_service('" + table_id + "'," + i + ")");
            i++;
        }
    })
}
function custom_sortOnTheadClick_service(table_id, colPo) {
    var trs_tbody = $("#" + table_id).find("tbody").find("tr");
    var tdA_mode = $(trs_tbody).eq(0).find("td").eq(colPo).text().trim();
    var tdB_mode = $(trs_tbody).eq(1).find("td").eq(colPo).text().trim();
    var mode;
    if (tdA_mode.length > tdB_mode.length) {
        mode = 1;
    } else if (tdA_mode.length < tdB_mode.length) {
        mode = -1;
    } else if (tdA_mode > tdB_mode) {
        mode = 1;
    } else {
        mode = -1;
    }
    trs_tbody.sort(function(trA, trB) {
        var tdA = $(trA).find("td").eq(colPo).text().trim();
        var tdB = $(trB).find("td").eq(colPo).text().trim();
        if (tdA.length > tdB.length) {
            return mode;
        } else if (tdA.length < tdB.length) {
            return -1 * mode;
        } else if (tdA > tdB) {
            return mode;
        } else {
            return -1 * mode;
        }
    });
    $("#" + table_id).append(trs_tbody);
}