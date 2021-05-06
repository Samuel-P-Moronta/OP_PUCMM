$(document).ready(function ($) {
    var array = [];
    $('#dataTable tbody').on('click', '#editar', function () {
        var curRow = $(this).closest('tr');
        var id = curRow.find('td:eq(0)').text();
        var nombre = curRow.find('td:eq(2)').text();
        var sector = curRow.find('td:eq(3)').text();
        var role = curRow.find('td:eq(4)').text();

        document.formEdit.idEdit.value = id;
        document.formEdit.fullNameEdit.value = nombre;
        document.formEdit.sectorEdit.value = sector;
        document.formEdit.academicLevelEdit.value = role;
    });
    $('#dataTable tbody').on('click', '#borrar', function () {
        var Row = $(this).closest('tr');
        var id = Row.find('td:eq(0)').text();
        document.f2.idBorrar.value = id;
    });
});
