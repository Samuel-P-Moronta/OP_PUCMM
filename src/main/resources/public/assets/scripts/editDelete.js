$(document).ready(function($){
    var array = [];
    $('#tabledit tbody').on('click','#editButtom',function(){
        var curRow = $(this).closest('tr');

        var col1 = curRow.find('td:eq(0)').text();
        array.push(col1);
        var col2 = curRow.find('td:eq(1)').text();
        array.push(col2);
        var col3 = curRow.find('td:eq(2)').text();
        array.push(col3);
        var col4 = curRow.find('td:eq(3)').text();
        array.push(col4);
        var col5 = curRow.find('td:eq(4)').text();
        array.push(col5);
        var result = col1+ '\n'+col2+ '\n'+col3+ '\n'+col4+ '\n'+col5;
        // console.log(array);
        //alert(array);
        loadModal(array);
    });
    //console.log(array);

    var modal = '';
    function loadButtom(){
        modal+='<div class="btn-group" role="group" aria-label="Basic example"><button type="button" class="btn btn-warning btn-rounded" id="editButtom" data-toggle="modal" data-target="#modalLoginForm"><i class="fas fa-user-edit fa-1x"></i></button><button type="button" data-toggle="modal" class="btn btn-danger btn-rounded" data-target="#ModalDanger" id="deleteButtom"><i class="fas fa-trash-alt fa-1x"></i></button> </div>'
        $(document).find('.modal_form').html(modal);
    }
    loadButtom();

    function loadModal(array) {
        var modal =''
        modal += '<form action="/update" method="POST">'
        modal +='<div class="modal fade" id="modalLoginForm" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">'
        modal += '<div class="modal-dialog" role="document">'
        modal += '<div class="modal-content">'
        modal+= '<div class="modal-header text-center">'
        modal += '<h4 class="modal-title w-100 font-weight-bold">Modificar Estudiante</h4>'
        modal += '<button type="button" class="close" data-dismiss="modal" aria-label="Close" onClick="window.location.reload();">'
        modal += ' <span aria-hidden="true">&times;</span>'
        modal += '</button>'
        modal += '</div>'
        modal += '<div class="modal-body mx-3">'

        modal += '<div class="md-form mb-2">'
        modal += '<i class="fas fa-id prefix grey-text"></i>'
        modal += '<input type="hidden" id="defaultForm-email" class="form-control validate" value="'+array[0]+'" name="id">'

        modal += '</div>'

        modal += '<div class="md-form mb-2">'
        modal += '<i class="fas fa-id prefix grey-text"></i>'
        modal += '<input type="number" id="defaultForm-email" class="form-control validate" value="'+array[1]+'" name="student_id">'

        modal += '</div>'


        modal += '<div class="md-form mb-2">'
        modal += '<i class="fas fa-signature prefix grey-text"></i>'
        modal += '<input type="name" id="defaultForm-email" class="form-control validate" value="'+array[2]+'" name="name">'

        modal += '</div>'

        modal += '<div class="md-form mb-2">'
        modal += '<i class="fas fa-signature prefix grey-text"></i>'
        modal += '<input type="name" id="defaultForm-email" class="form-control validate" value="'+array[3]+'" name="last_name">'

        modal += '</div>'

        modal += '<div class="md-form mb-2">'
        modal += '<i class="fas fa-phone prefix grey-text"></i>'
        modal += '<input type="number" id="defaultForm-email" class="form-control validate" value="'+array[4]+'" name="tel">'

        modal += '</div>'


        modal += '<div class="modal-footer d-flex justify-content-center"><button class="btn btn-default" type="submit">Modificar</button></div>'
        modal += '</div>'
        modal += '</div>'
        modal += '</div>'
        modal += '</form>'
        $(document).find('.modal_form').html(modal);

    }
    function loadModalDelete(darray){
        var deleteMod = ''
        deleteMod+='<form action="/delete" method="post">'
        deleteMod += '<!-- Central Modal Danger Demo--> <div class="modal fade right" id="ModalDanger" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="t`rue">'
        deleteMod+='<div class="modal-dialog modal-notify modal-danger modal-side modal-top-right" role="document">'
        deleteMod+= '<div class="modal-content">'

        deleteMod+='<div class="modal-header">'
        deleteMod+='<p class="heading">ELIMINAR ESTUDIANTE</p>'
        deleteMod+='<button type="button" class="close" data-dismiss="modal" aria-label="Close" onClick="window.location.reload();"><span aria-hidden="true" class="white-text">&times;</span></button>'
        deleteMod+='</div>'


        deleteMod+='<div class="modal-body">'
        deleteMod+='<div class="row">'
        deleteMod+='<div class="col-3">'
        deleteMod+='<p></p>'
        deleteMod+='<p class="text-center"><i class="fas fa-user-times fa-4x"></i></p>'
        deleteMod+='</div>'

        deleteMod+='<div class="col-9">'
        deleteMod+= '<input type="hidden" id="defaultForm-email" class="form-control validate" value="'+darray[0]+'" name="id">'
        deleteMod+='<p>Esta seguro de que desea eliminar al estudiante '+darray[2]+' '+darray[3]+'</p>'

        deleteMod+='</div>'
        deleteMod+='</div>'
        deleteMod+='</div>'


        deleteMod+='<div class="modal-footer justify-content-center">'
        deleteMod+='<button type="submit" class="btn btn-danger btn-rounded">Si, Estoy Seguro!</button>'
        deleteMod+='<button type="button" class="btn btn-warning btn-rounded" onClick="window.location.reload();">No</button>'
        //deleteMod+='<a type="button" class="btn btn-danger">Si!Estoy seguro.<i class="far fa-gem ml-1 white-text"></i></a>'
        //deleteMod+='<a type="button" class="btn btn-outline-danger waves-effect" data-dismiss="modal">No</a>'
        deleteMod+='</div>'
        deleteMod+='</div>'

        deleteMod+='</div>'
        deleteMod+='</div>'
        deleteMod+='</form>'
        $(document).find('.modal_form').html(deleteMod);
    }
});
