<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>API - ${context}</title>
 

  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
   <style>

   div {
	word-wrap: break-word;
}

pre{
	text-align: left;
	background-color: rgb(244,240,244);
}
    </style>
</head>
<body>
	 
	<h1></h1>	
	<table class="table table-striped" >
 	 <tr >
	<td style="border: 1px solid black; " width="25%"><h3>Functionality</h3></td>
    <td style="border: 1px solid black;" width="15%"><h3>Type</h3></td>
    <td style="border: 1px solid black;" width="60%"><h3>Request Parameters</h3></td> 
   
   </tr>
    <tr>
    <td style="border: 1px solid black;">API Details</td>
    <td style="border: 1px solid black;"><h3>GET</h3></td>
  	<td style="border: 1px solid black;">
  	<div class="container">
 		<a data-target="#demo" data-toggle="collapse" href="#demo"> ${IP}${context}/api/doc</a>
 		 <div  width="50" id="demo" class="collapse">
			Api Details
  		</div>
	</div></td>
 </tr>
 
 
    <tr>
    <td style="border: 1px solid black;">Request access Token</td>
    <td style="border: 1px solid black;"><h3>POST</h3></td>
	<td style="border: 1px solid black;">
  	<div class="container">
  	 <a data-target="#token" data-toggle="collapse" href="#token"> ${IP}${context}/oauth/token</a>
 		 <div width="50" id="token" class="collapse">
		Request Username and Password for oauth:<br>
		<pre>clientId: gmJHRe0BqCZLA8kOQzBKPmmxwjHEou1SZp3Lb6K0<br>clientSecret: 3uucRwadqPRcqbUQO3rFeLQerze83ZG4mnyAFzMS</pre>
Response: 200<br><pre>{
  "access_token": "2a403934-0354-43c8-a607-a489887b5668",
  "token_type": "bearer",
  "expires_in": 86400,
  "scope": "trust"
}
</pre> 
<BR>Response : 401
<pre>
{
  "error": "invalid_client",
  "error_description": "A new version of Liv. is available. Please update app to new version to continue Liv.ing"
}
</pre>

		</div>
	</div></td>
  </tr>
 
 		<!--<tr>
			<td style="border: 1px solid black;">Connect</td>
			<td style="border: 1px solid black;"><h3>GET</h3></td>
			<td style="border: 1px solid black;">
				<div class="container">
					<a data-target="#demo7" data-toggle="collapse" href="#demo7">
						${IP}${context}/api/connect</a>
					<div width="50" id="demo7" class="collapse">
						Request:<br>
						<pre></pre>
						 Response:<br>
						<pre>Status 200:<br>{
  "result": "Success"
}</pre><br>
					
						</pre>

					</div>
				</div>
			</td>
		</tr>
		-->
		<tr>
			<td style="border: 1px solid black;">Get Customer</td>
			<td style="border: 1px solid black;"><h3>POST</h3></td>
			<td style="border: 1px solid black;">
				<div class="container">
					<a data-target="#getAccounts" data-toggle="collapse" href="#getAccounts">
						${IP}${context}/api/getCustomer</a>
					<div width="50" id="getAccounts" class="collapse">
						Request:<br>
						<pre>
{
 "authCode":"K+u0K8R52vMZErhD4Yt4h50nvbFQA0QMOrUOE2PcMgw="
}
						</pre>
						 Response:<br>
						<pre>Status 200:<br>{
  "result": "success",
  "customer": {
    "email": "DINU@GMAIL.COM",
    "name": "NAME47684966"
  }
}</pre>
					
						</pre>

					</div>
				</div>
			</td>
		</tr>
 
 <tr>
			<td style="border: 1px solid black;">Validate Transfer</td>
			<td style="border: 1px solid black;"><h3>POST</h3></td>
			<td style="border: 1px solid black;">
				<div class="container">
					<a data-target="#valtransfer" data-toggle="collapse" href="#valtransfer">
						${IP}${context}/api/mePay</a>
					<div width="50" id="valtransfer" class="collapse">
						Request:<br>
						<pre>
{
  "authCode": "K+u0K8R52vMZErhD4Yt4h50nvbFQA0QMOrUOE2PcMgw=",
  "transfer": {
    "amount": "5",
    "mobileNo": "971552658232"
  }
}
						</pre>
						<br> Response:<br>
						<pre>Status 200:<br>{
  "transfer": {
    "amount": 10,
    "mobileNo": "971552658232"
  }
}</pre><br>
					
						</pre>

					</div>
				</div>
			</td>
		</tr>
		
		<tr>
			<td style="border: 1px solid black;">Transfer</td>
			<td style="border: 1px solid black;"><h3>POST</h3></td>
			<td style="border: 1px solid black;">
				<div class="container">
					<a data-target="#transfer" data-toggle="collapse" href="#transfer">
						${IP}${context}/api/balance</a>
					<div width="50" id="transfer" class="collapse">
						Request:<br>
						<pre>{
 "authCode":"K+u0K8R52vMZErhD4Yt4h50nvbFQA0QMOrUOE2PcMgw="
}
	</pre>
						 Response:<br>
						<pre>Status 200:<br>{
  "balance": 6805.37
}</pre><br>
					
						</pre>

					</div>
				</div>
			</td>
		</tr>
  
  
  
	</table>
</body>
</html>