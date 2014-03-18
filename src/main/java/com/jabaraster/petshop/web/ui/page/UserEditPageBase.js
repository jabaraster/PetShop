function setBootstrapSwitch() {
	var check = $('form input[type="checkbox"]');
	check.bootstrapSwitch();
	check.bootstrapSwitch('onText', '管理者');
	check.bootstrapSwitch('offText', '通常');
}

$(function() {
	setBootstrapSwitch();
	$('form input').validateEngine();
});