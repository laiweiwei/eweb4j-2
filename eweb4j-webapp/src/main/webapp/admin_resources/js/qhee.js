/**
 * 检索树形菜单。
 * 几个约定好的配置：
 *   1.搜索表单中的 <select name="searchField" <input name="searchKeyword"
 *   2.树形菜单表单中的能被搜索到的节点增加一个属性 searchable=true
 *   3.属性菜单表单中的能被搜索到的节点增加跟搜索表单中的<select><option>一一对应的参数键值对，比如：
 * <code>
 *    <form id="search_form" onsubmit="return search_tree(this,'tree_form')">
 *        <select name="searchField">
 *          <option value="field_1">xxx</option>
 *          <option value="field_2">ooo</option>
 *        </select>
 *    </form>
 *    <form id="tree_form">
 *        <ul class="tree">
 *          #foreach($pojo in $pojos)
 *          <li><a searchable=true field_1="$pojo.field_1" field_2="$pojo.field_2" tvalue="$pojo.id">$pojo.name/a>
 *          #end
 *        </ul>
 *    </form>
 *    
 * </code>
 * @author laiweiwei
 * @date 2013-07-06 下午16:49
 * @param json 参数：{search_form:执行搜索的表单对象, tree_form:树形菜单所在的表单对象}
 */
function search_tree(json){
    var $search_form = $(json.search_form);
    var $tree_form = $(json.tree_form);
    var searchField = $search_form.find("select[name='searchField']").val();
    var fields = $search_form.find("select[name='searchField']").find("option[value != '']");
    
	var aList = $tree_form.find("a[searchable=true]");
	if (aList && aList.length <= 0)
		return false;
	aList.each(function(i){
	    $(this).css('background','none');
	});

    var keyword = $search_form.find("input[name='searchKeyword']").val();
    if (!keyword || keyword == "")
        return false;
    
	var $div_to_scroll = $tree_form.find("div[class='pageFormContent']");
	//var height = $.pdialog.getCurrent().height();
	var offset1 = $div_to_scroll.offset().top + 5;

	var lastA ;
    aList.each(function(i){
        var a = $(this);
	    var isOK = false;
	    if (searchField == "") {
	        fields.each(function(i){
		        var fieldName = $(this).attr('value');
		        var fieldValue = a.attr(fieldName);
		        if (fieldValue.indexOf(keyword) >= 0){
	                isOK = true;
		            return ;
	            }
            });
	    } else {
	        fields.each(function(i){
		        var fieldName = $(this).attr('value');
		        var fieldValue = a.attr(fieldName);
		        if (fieldName == searchField && fieldValue.indexOf(keyword) >= 0){
	                isOK = true;
		             return ;
	            }
            });
	    } 
	    if (isOK) {
	        //高亮显示被搜索到的节点
	        a.css('background','lightgreen');
			lastA = a;
	    }
    });

	if (!lastA)
		return false;
	
	//先将滚动条移动到最顶
	$div_to_scroll.animate({scrollTop:0},"fast", function(){
	    //等完成后回调此函数，再次进行滚动
	    var offset2 = lastA.offset().top;
	    var offset = offset2-offset1;
		if ((offset > 0)) {
		    var json = {scrollTop:offset};
	        $div_to_scroll.animate(json,"slow");
	    }
	});//先移动到最顶

    return false;
}

/**
 * 点击行表格的时候勾选左边的checkbox
 */
function selectBox(id){
	var checkbox = document.getElementById(id);
	if (checkbox != null){
		checkbox.checked = 1-checkbox.checked;
	}
}