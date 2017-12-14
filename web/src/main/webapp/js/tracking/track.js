function trackCalculateX(posX) {
	return 1184 - (23.68 * posX);
}

function trackCalculateY(posY) {
	return 685 - 24.46 * posY;
}

function clearSelectElement(id) {
	try {
		var select = document.getElementById(id);
		while (select.size != 0) {
			select.remove(0);
		}
	} catch (error) {
		throw "Function :: clearSelectElement Error: " + error;
	}
}

function fillSelectElement(id, elements) {
	try {
		var select = document.getElementById(id);
		var length = elements.length;
		var i = 0;
		for (i; i < length; i++) {
			var option = document.createElement("option");
			option.text = elements[i];
			option.value = elements[i];
			select.add(option);
		}
	} catch (error) {
		throw "Function :: fillSelectElement Error: " + error;
	}
}

function clearAndFillSelectElement(id, elements) {
	try {
		clearSelectElement(id);
		fillSelectElement(id, elements);
	} catch (error) {
		throw "Function :: clearAndFillSelectElement Error: " + error;
	}
}

function fillDataTableWithValues(dataTable, data) {
	try {
		var length = data.length;
		var i = 0;
		for (i; i < length; i++) {
			var row = [ data[i].position.uuid, data[i].position.zone.id,
					data[i].position.zone.name, data[i].position.coordinate.x,
					data[i].position.coordinate.y,
					data[i].position.coordinate.z, new Date(data[i].date) ];
			dataTable.row.add(row).draw(false);
			// console.log("ROW: " + row);
		}
	} catch (error) {
		throw "Function :: fillDataTableWithValues Error: " + error;
	}
}

function drawArea(map, data) {
	try {
		var length = data.length;
		var i = 0;
		for (i; i < length; i++) {
			map.append("rect").attr("x", data[i].startX).attr("y",
					data[i].startY).attr("height",
					data[i].endY - data[i].startY).attr("width",
					data[i].endX - data[i].startX).attr("fill", "none").attr(
					"stroke", "red").attr("stroke-width", 1);

		}
	} catch (error) {
		console.log(error);
	}
}

function drawGraphPoints(map, data) {
	try {
		for (i in data) {
			map.append("circle").attr("cx", data[i].posX).attr("cy",
					data[i].posY).attr("r", 3);
		}
	} catch (error) {
		throw "Function :: drawGraphPoints Error: " + error;
	}
}

function createMap(div, imageSource) {
	try {

		$("#" + div).html("");
		floorMapSVG = d3.select("#" + div).append("svg").attr("width", "100%")
				.attr("height", "100%").attr("viewBox", "0 0 1196 705");
		floorMapSVG.append("image").attr("xlink:href", imageSource)
				.attr("x", 0).attr("y", 0).attr("width", 1196).attr("height",
						705);
		return floorMapSVG;
	} catch (error) {
		throw "Function :: createMap Error: " + error;
	}
}

function drawPositions(positions, floors, marker) {
	try {
		var length = positions.length;
		var i = 0;
		for (i; i < length; i++) {
			var z = positions[i].position.coordinate.z;
			var posX = trackCalculateX(positions[i].position.coordinate.x);
			var posY = trackCalculateY(positions[i].position.coordinate.y);
			if (z <= 3) {
				floors[0].append("circle").attr("cx", posX).attr("cy", posY)
						.attr("r", 5);
				floors[0].append("image").attr("xlink:href", marker).attr("x",
						posX - 15).attr("y", posY - 30).attr("width", 30).attr(
						"height", 30);
				continue;
			}

			if (3 < z && z <= 6) {
				floors[1].append("circle").attr("cx", posX).attr("cy", posY)
						.attr("r", 5);
				floors[1].append("image").attr("xlink:href", marker).attr("x",
						posX - 15).attr("y", posY - 30).attr("width", 30).attr(
						"height", 30);
				continue;
			}

			floors[2].append("circle").attr("cx", posX).attr("cy", posY).attr(
					"r", 5);
			floors[2].append("image").attr("xlink:href", marker).attr("x",
					posX - 15).attr("y", posY - 30).attr("width", 30).attr(
					"height", 30);

		}
	} catch (error) {
		throw "Function :: drawPositions Error: " + error;
	}
}

function calculateFloor(position) {
	try {
		var coorZ = position.position.coordinate.z;
		if (coorZ < 3) {
			return 0;
		}
		if (3 < coorZ && coorZ < 6) {
			return 1;
		}
		return 2;
	} catch (error) {
		throw "Function :: calculateFloor Error: " + error;
	}
}

function sortPositionsByTime(positions) {
	try {
		positions.sort(function(a, b) {
			return a.date - b.date;
		});
	} catch (error) {
		throw "Function :: sortPositionsByTime Error: " + error;
	}
}

function generatePath(positions, url) {
	try {
		sortPositionsByTime(positions);
		var length = positions.length;
		if (length <= 1) {
			return;
		}
		var i = 0;
		for (i; i < length; i++) {
			if (i == length - 1) {
				break;
			}
			floorFirst = calculateFloor(positions[i]);
			floorSecond = calculateFloor(positions[i + 1]);

			if (floorFirst == floorSecond) {

				getThePathFromTheServer(positions[i], positions[i + 1], url);
			} else {
				console.log("generatePath else ág!!!");
				return;
			}
		}
	} catch (error) {
		throw "Function :: generatePath Error: " + error;
	}
}

function getFloorByNumber(floorNumber) {
	switch (floorNumber) {
	case 0:
		return ZonesGroundFloor;
	case 1:
		return ZonesFirstFloor;
	case 2:
		return ZonesSecondFloor;
	default:
		throw "Invalid floor number!";
	}
}

function getNodesByNumber(floorNumber) {
	switch (floorNumber) {
	case 0:
		return graphNodesGroundFloor;
	case 1:
		return graphNodesFirstFloor;
	case 2:
		return graphNodesSecondFloor;
	default:
		throw "Invalid floor number!";
	}
}

function calculateNearestNode(position, floor) {
	try {
		var floor = getFloorByNumber(floor);
		var zone = null;
		var posX = trackCalculateX(position.position.coordinate.x);
		var posY = trackCalculateY(position.position.coordinate.y);
		for ( var area in floor) {
			if (floor[area].startX <= posX && posX <= floor[area].endX) {
				if (floor[area].startY <= posY && posY <= floor[area].endY) {
					zone = floor[area];
					break;
				}
			}
		}
		if (zone == null) {
			throw "Function :: calculateNearestNode Invalid zone: zone is null!";
		}
		var points = zone.Points;
		var length = points.length;
		var i = 1;
		if (length == 0) {
			throw "Function :: calculateNearestNode Invalid points length : 0!";
		}
		var nearestNode = points[0];
		var distance = Math.sqrt(Math.pow(nearestNode.posX - posX, 2)
				+ Math.pow(nearestNode.posY - posY, 2));
		for (i; i < length; i++) {
			var calcDistance = Math.sqrt(Math.pow(posX - points[i].posX, 2)
					+ Math.pow(posY - points[i].posY, 2));
			if (calcDistance < distance) {
				distance = calcDistance;
				nearestNode = points[i];
			}
		}
		return nearestNode;
	} catch (error) {
		throw "Function :: calculateNearestNode Error: " + error;
	}
}

function getThePathFromTheServer(startPos, endPos, url) {
	try {
		var startNode = calculateNearestNode(startPos, calculateFloor(startPos));
		var endNode = calculateNearestNode(endPos, calculateFloor(endPos));
		$
				.ajax({
					type : "POST",
					async : true,
					timeout : 10000,
					url : url,
					beforeSend : function(xhr) {
						xhr.setRequestHeader($("meta[name='_csrf_header']")
								.attr("content"), $("meta[name='_csrf']").attr(
								"content"));
					},
					data : {
						start : startNode.id,
						end : endNode.id
					},
					success : function(result, status, xhr) {
						try {
							var points = generatePathInputData(startPos,
									endPos, result);
							drawPath(
									getFloorByNumber(calculateFloor(startPos)),
									points);
						} catch (error) {
							console.log(error);
						}
					},
					error : function(xhr, status, error) {
						console.log(error);
					}
				});
	} catch (error) {
		throw "Function :: getThePathFromTheServer Error: " + error;
	}
}

function generatePathInputData(startPos, endPos, pathPoints) {
	try {
		nodes = getNodesByNumber(calculateFloor(startPos));
		var points = [];
		points.push({
			"x" : trackCalculateX(startPos.position.coordinate.x),
			"y" : trackCalculateY(startPos.position.coordinate.y)
		});
		var length = pathPoints.length;
		var i = 0;
		for (i; i < length; i++) {
			var point = nodes[pathPoints[i]];
			points.push({
				"x" : point.posX,
				"y" : point.posY
			})
		}
		points.push({
			"x" : trackCalculateX(endPos.position.coordinate.x),
			"y" : trackCalculateY(endPos.position.coordinate.y)
		});
		return points;
	} catch (error) {
		throw "Function :: generatePathInputData Error: " + error;
	}
}

function drawPath(floorMap, data) {
	try {
		var lineGrapha = d3.line().x(function(d) {
			return d.x;
		}).y(function(d) {
			return d.y;
		}).curve(d3.curveCatmullRom.alpha(0.5))// .curve(d3.curveBasis); // v4
		firstFloorMap.append("path").attr("d", lineGrapha(data)).attr("stroke",
				"blue").attr("stroke-width", 2).attr("fill", "none");
	} catch (error) {
		throw "Function :: drawPath Error: " + error;
	}
}
