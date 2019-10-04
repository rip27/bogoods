@extends('apps.layout')

@section('content')
    <h1>Data Pembayaran</h1>
    <div class="col-md-12">
    <div class="card card-default">
        <div class="card-header">
            <div class="row">
                <div class="col-md-10">
                    <strong>All Order Need Approved</strong>
                </div>
            </div>
        </div>

        <div class="card-body">
            <table class="table table-bordered" id="table_data" width="100%">
                <thead>
                    <tr>
                            <th>No.</th>
                            <th>Order ID</th>
                            <th>Bukti Bayar</th>
                            <th>Tanggal Pesan</th>
                            <th>Total Bayar</th>
                            <th>Jenis Pembayaran</th>
                            <th>Status Bayar</th>
                            <th>Status Barang</th>
                            <th width="180" class="text-center">Action</th>
                    </tr>
                </thead>
            </table>
        </div>
    </div>
</div>
    

  
      <!-- /.modal -->
@stop
<script src="https://www.gstatic.com/firebasejs/5.10.1/firebase.js"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
<script type="text/javascript" src="{{URL::asset('js/jquery.dataTables.min.js')}}"></script>
<script type="text/javascript" src="{{URL::asset('js/datatables.bootstrap.min.js')}}"></script>
        <script>
        var firebaseConfig = {
    apiKey: "AIzaSyB9yetTRoVua3jP89ReJzZQ4QQnKqDIrr4",
    authDomain: "bogoods.firebaseapp.com",
    databaseURL: "https://bogoods.firebaseio.com",
    projectId: "bogoods",
    storageBucket: "bogoods.appspot.com",
    messagingSenderId: "912276967847",
    appId: "1:912276967847:web:b524f8b6ff9c568c6c6682"
  };
  
  $(function () {
      var obj = [];
      var obj2 = [];
      var no = 1;
      firebase.database().ref('order/').on('value', function(snapshot) {
          var order = snapshot.val();
          obj = [];  
          $.each(order, function(index ,order){
              if(order) {
                  obj2 = [no++,order.idorder,'<img width="125px" maxheigth="125px" src='+ order.buktitforder +' />',order.tglpesan,order.totalbayar,order.pembayaran,order.statuspembayaran,order.statusbarang,'<a data-toggle="modal"  class="btn btn-success updateData" onclick="openModal(\'' + index + '\')" data-id="'+index+'">Approve</a>\
                  <a data-toggle="modal" data-target="#remove-modal" class="btn btn-danger removeData" data-id="'+index+'">Reject</a>'];
                  obj.push(obj2);
                }
            });
            addTable(obj);
            function addTable(data){
                $('#table_data').DataTable().clear().draw();
                $('#table_data').DataTable().rows.add(data).draw();
            }
        });
    });

    firebase.initializeApp(firebaseConfig);
    
    function openModal(index){
        // alert(index);
        $('#update-modal').modal('show');
        $('#id_index').val(index);
    }

    $('#body').on('click', '.updateData', function() {
        var updateID = $(this).attr('data-id');
        firebase.database().ref('store/' + updateID).on('value', function(snapshot) {
            var values = snapshot.val();
        });
    });

 function updateUserRecord() {
     var updateID = $('#id_index').val();
        firebase.database().ref('order/' +updateID).on('value', function(snapshot) {
    var data = snapshot.val();
    alert("SUKSES")
	var postData = {
        alamatpengiriman:data.alamatpengiriman,
        buktitforder:data.buktitforder,
        idpemilikstore:data.idpemilikstore,
        idpembeli:data.idpembeli,
        idbarang:data.idbarang,
        idorder:data.idorder,
        idstore:data.idstore,
        statuspembayaran:"konfirmasiseller",
        statusbarang:data.statusbarang,
        jumlah:data.jumlah,
        pembayaran:data.pembayaran,
        pengiriman:data.pengiriman,
        tglpesan:data.tglpesan,
        total:data.total,
        totalbayar:data.totalbayar,
    };
    
	var updates = {};
	updates['order/' + updateID] = postData;
	firebase.database().ref().update(updates);
	$("#update-modal").modal('hide');
    });
}

  </script>
  
  <form action="" method="POST" class="users-remove-record-model form-horizontal">
        <div id="remove-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel"
            aria-hidden="true" style="display: none;">
            <div class="modal-dialog" style="width:55%;">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4 class="modal-title" id="custom-width-modalLabel">Reject Order</h4>
                        <button type="button" class="close remove-data-from-delete-form" data-dismiss="modal"
                            aria-hidden="true">×</button>
                    </div>
                    <div class="modal-body" id="rejectBody">
    
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default waves-effect remove-data-from-delete-form"
                            data-dismiss="modal">Close</button>
                        <button type="button"
                            class="btn btn-danger waves-effect waves-light rejectMatchRecord">Reject</button>
                    </div>
                </div>
            </div>
        </div>
    </form>
    <!-- Update Model -->
    <form action="" method="POST" class="users-update-record-model form-horizontal">
        <div id="update-modal" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="custom-width-modalLabel" aria-hidden="true" style="display: none;">
            <div class="modal-dialog" style="width:55%;">
                <div class="modal-content" style="overflow: hidden;">
                    <div class="modal-header">
                        <h4 class="modal-title" id="custom-width-modalLabel">Update Record</h4>
                        <button type="button" class="close update-data-from-delete-form" data-dismiss="modal" aria-hidden="true">×</button>
                    </div>
                    <input type="hidden" id="id_index">
                    <div class="modal-body" id="updateBody">
                        <h4>Are you sure want to approve this order?</h4>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default waves-effect update-data-from-delete-form" data-dismiss="modal">Close</button>
                        <button type="button" class="btn btn-success waves-effect waves-light" onclick="updateUserRecord()">Uasddaspdate</button>
                    </div>
                </div>
            </div>
        </div>
    </form>