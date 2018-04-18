$(document).ready(() => {
    var $salvar = $('#btn-salvar'); //document.getElementById("btn_salvar");
    var $nome = $('#cmp-nome');
    $nome.on('keyPress', () => {
        if ($nome.val() === undefined || $nome.val() === "" || $nome.val() === null) {
            $salvar.prop('disabled', () => { return 'disabled'; });
        } else {
            $salvar.prop('disabled', () => { return undefined; });
        }
        return;
    });
});


//function validar() {
//    var $salvar = $('#btn-salvar'); //document.getElementById("btn_salvar");
//    var $nome = $('#nome');
//    if ($nome.value === undefined || $nome.value === "" || $nome.value === null) {
//        salvar.disabled = "disabled";
//        return;
//    }
//    salvar.disabled = undefined;
//}