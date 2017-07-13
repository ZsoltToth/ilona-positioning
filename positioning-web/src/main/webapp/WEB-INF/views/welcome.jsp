	<script type="text/javascript">
	function loadZones() {
		$.getJSON("listZones", function(data) {
			$.each(data, function(index, value) {
				$("#content").html(printZoneTree(value));
			});
		});
	}

	function printZoneTree(zone) {
		var container = $("<ul>");
		$("<li>", {
			text : zone.name
		}).appendTo(container);
		if (!zone.children) {
			return container;
		}
		$.each(zone.children, function(i, child) {
			container.append(printZoneTree(child));
		});
		return container;
	}

	function loadAddZoneForm() {
		$.post("addZoneForm", function(data, status) {
			$("#content").html(data);
		});
	}

	function loadRecordMeasurement() {
		$("#content").html("List of Measurements");
		/*
		$.get("recordMeasurement", {
			"zoneId" : "b4bb64f5-5a17-446a-93ec-71170e783610",
			"values" : {
				"AP1" : -75.0,
				"AP2" : -63.0
			}
		}, function(data, status) {
			$("#content").html(data);
		});
		 */
	}

	function loadOptions() {

		$("#content").load("positioningSetup");
	}

	function loadLogs() {
		$("#content").html("Logs");
	}

	function getLocation() {
		$.get("getLocation", {
			"zoneId" : "b4bb64f5-5a17-446a-93ec-71170e783610",
			"values" : {
				"AP1" : -75.0,
				"AP2" : -63.0
			}
		}, function(data, status) {
			$("#content").html(data);
		});
	}
</script>
	
	<header>
		<div class="header-inner">
			<div class="navbar navbar-inverse ">
				<div class="container">
					<div class="navbar-header">
						<button type="button" class="navbar-toggle" data-toggle="collapse"
							data-target=".navbar-collapse">
							<span class="icon-bar"></span> <span class="icon-bar"></span> <span
								class="icon-bar"></span>
						</button>
					</div>

					<div class="collapse navbar-collapse navbar-ex1-collapse">
						<ul class="nav navbar-nav">
							<li><a href="#home" class="home">HOME</a></li>
							<li><a href="#sec-what-we-do" class="wwd">WHAT WE DO</a></li>
							<li><a href="#about-us" class="about">ABOUT US</a></li>
						</ul>
					</div>
				</div>
			</div>

			<h1 class="home-head">Welcome</h1>

			

			<button class="more">
				<a
					href="http://www.iit.uni-miskolc.hu/iitweb/opencms/users/tothzs/research/IPS"
					target="_blank">READ ABOUT US</a>
			</button>
			<br>
			<button class="more" onclick="loadAddZoneForm();">Add new
				Zone</button>
			<button class="more" onclick="loadZones();">List Zones</button>
			<button class="more" onclick="loadRecordMeasurement();">List
				Measurements</button>
			<button class="more" onclick="loadOptions();">Options</button>
			<button class="more" onclick="loadLogs();">Show Logs</button>
		</div>

	</header>

	<div id="sec-what-we-do">
		<div class="container">
			<div class="row">
				<div class="col-md-12">

					<div class="services" id="content"></div>
				</div>
			</div>
		</div>
	</div>

	<div class="about-us" id="about-us">
		<div class="container">
			<div class="row">
				<h1>About us</h1>

				<div class="col-md-4">
					<p>Vivamus et consectetur orci. Sed at viverra leo, et sodales
						nunc. Fusce dapibus volutpat vestibulum. Integer mollis cursus
						convallis. Maecenas elementum tortor quis magna ullamcorper, ut
						convallis metus eleifend.</p>
				</div>

				<div class="col-md-4">
					<p>Vivamus et consectetur orci. Sed at viverra leo, et sodales
						nunc. Fusce dapibus volutpat vestibulum. Integer mollis cursus
						convallis. Maecenas elementum tortor quis magna ullamcorper, ut
						convallis metus eleifend.</p>
				</div>

				<div class="col-md-4">
					<p>Vivamus et consectetur orci. Sed at viverra leo, et sodales
						nunc. Fusce dapibus volutpat vestibulum. Integer mollis cursus
						convallis. Maecenas elementum tortor quis magna ullamcorper, ut
						convallis metus eleifend.</p>
				</div>
			</div>
		</div>
	</div>