function setBootstrapSwitch() {
	var check = $('form input[type="checkbox"]');
	check.bootstrapSwitch();
	check.bootstrapSwitch('onText', '管理者');
	check.bootstrapSwitch('offText', '通常');
}

$(function() {
//	$('form').validationEngine();
//	$('form input[type="checkbox"]').click(function() {
//		alert($(this).prop('checked'));
//	});
});