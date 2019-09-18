@extends('apps.layout')

@section('content')

    <h1>Data Store</h1>
    <div class="container">
        <div class="row">
            <div class="col-md-12">
                <div class="card card-default">
                    <div class="card-header">
                        <div class="row">
                            <div class="col-md-10">
                                <strong>All Store Listing</strong>
                            </div>
                        </div>
                    </div>
    
                    <div class="card-body">
                        <table class="table table-bordered">
                            <tr>
                                <th>No.</th>
                                <th>Store Name</th>
                                <th>Owner</th>
                                <th>Address</th>
                                <th>Status</th>
                                <th width="180" class="text-center">Action</th>
                            </tr>
                            <tbody id="tbody">
                                
                            </tbody>	
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    

@stop
<script src="https://www.gstatic.com/firebasejs/5.10.1/firebase.js"></script>
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
  // Initialize Firebase
  firebase.initializeApp(firebaseConfig);

  var databaseRef = firebase.database().ref('store/');
  var lastIndex = 0;

  databaseRef.on("value", function(snapshot) {
    var value = snapshot.val();
    var htmls = [];
    var no = 1;
    $.each(value, function(index, value){
    	if(value) {
                htmls.push('<tr>\
        		<td>'+ no++ +'</td>\
        		<td>'+ value.storename +'</td>\
        		<td>'+ value.idpemilik +'</td>\
        		<td>'+ value.address +'</td>\
        		<td>'+ value.status +'</td>\
        		<td><a class="btn btn-outline-success approveStore" id="'+index+'">Approve</a>\
        		<a class="btn btn-outline-danger rejectStore" id="'+index+'">Reject</a></td>\
        	    </tr>');
                $('#tbody').html(htmls);
    	}    	
    	lastIndex = index;
    });
    $(document).ready(function(){
          $(".approveStore").on("click",function(){
            firebase.database().ref('store/'+ index).remove();
            reload_page();
          });
        });
       
  });

  </script>