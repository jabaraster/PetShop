$(function() {
	$('ul.categories > li').click(function(e) {
		var check = $(this).find('input[type="checkbox"]');
		var checked = check.is(':checked');
		check.prop('checked', !checked);
	});
	$('ul.categories > li > label').click(function(e) {
		e.stopPropagation();
	});
});