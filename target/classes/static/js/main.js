var usernamePage = document.querySelector('#username-page');
var gamePage = document.querySelector('#game-page');
var manualPlayButton = document.querySelector('#manualPlayButton');
var automaticPlayButton = document.querySelector('#automaticPlayButton');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');
var adjustAbleParamForm = document.querySelector('#adjustAbleParamForm');
var adjustAbleParams = document.getElementById("adjustAbleParams");
var sendTheNumberForm = document.querySelector('#sendTheNumberForm');
var disconnectForm = document.querySelector('#disconnectForm');
var playerNameArea = document.querySelector('#playerNameArea');


var stompClient = null;
var gameId = null;
var playType = null;
var number = null;
var move = adjustAbleParams.options[adjustAbleParams.selectedIndex].value;
var from = null;
var to = null;
var playerId = null;
var playerName = null;
var toId = null;
var gameCreationTime = null;
var gameStatus = null;
var playerList = null;

function connect(event) {

	$.ajax({
		url: '/joinToTheGame',
		type: 'GET',
		cache: false,
		async: true,
		success: function(request) {
			console.log("gameId " + request.id)
			gameCreationTime = request.creationTime;
			gameId = request.id;
			gameStatus = request.gameStatus;
			playerList = request.playerList;
			if (playerList.length === 1) {
				playerId = request.playerList[0].id;
				playerName = request.playerList[0].name;
			}
			else {
				playerId = request.playerList[1].id;
				playerName = request.playerList[1].name;
			}

		}
	});
	setTimeout(function() {
		//connect to the websocket endpoint
		var socket = new SockJS('/ws');
		stompClient = Stomp.over(socket);
		stompClient.connect({}, onConnected, onError);

	}, 700);

	usernamePage.classList.add('hidden');
	gamePage.classList.remove('hidden');

	event.preventDefault();
}


function onConnected() {

	hideComponents();
	if (gameId === null) {
		alert('Subscription can not be done please refresh the page');
	}
	stompClient.subscribe('/topic/gameOfThree/' + gameId, onMessageReceived);

	connectingElement.classList.add('hidden');
}



function onError(error) {
	connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
	connectingElement.style.color = 'red';
}


function manualPlay(event) {

	hideComponents();
	if (stompClient) {
		var manualPlayMessage = {
			gameId: gameId,
			playType: 'MANUAL_PLAY',
			playerId: playerId
		};
		stompClient.send("/app/game.manualPlayGame", {}, JSON.stringify(manualPlayMessage));
	}
	event.preventDefault();
}

function automaticPlay(event) {
	hideComponents();
	if (stompClient) {
		var automaticPlayMessage = {
			gameId: gameId,
			playType: 'AUTOMATIC_PLAY',
			playerId: playerId
		};
		stompClient.send("/app/game.autoPlayGame", {}, JSON.stringify(automaticPlayMessage));
	}

	event.preventDefault();
}

function setMoveValue() {
	move = adjustAbleParams.options[adjustAbleParams.selectedIndex].value;
}

function sendTheNumber(event) {
	if (stompClient) {
		var sendMessage = {
			gameId: gameId,
			number: number,
			move: move,
			playerId: playerId
		};
		stompClient.send("/app/game.calculateNumber", {}, JSON.stringify(sendMessage));
	}
	event.preventDefault();
}

function onMessageReceived(payload) {

	var message = JSON.parse(payload.body);
	var messageElement = document.createElement('li');

	if (message.gameStatus === 'WAITING_FOR_PLAYER') {
		hideComponents();
		gameId = message.gameId;
		gameStatus = message.gameStatus;
		message.content = playerName + ' is waiting for a player!';
		messageElement.classList.add('event-message');
		alert("Waiting for a player");
	}
	else if (message.gameStatus === 'STARTED') {
		hideButtons();
		visibleComponents();

		var messagePlayerName = document.createTextNode(playerName);
		var textElement = document.createElement('p');
		messageElement = document.createElement('li');
		textElement.appendChild(messagePlayerName);
		messageElement.appendChild(textElement);
		playerNameArea.appendChild(messageElement);

		gameId = message.gameId;
		to = message.to;
		toId = message.toId;
		number = message.number;
		playType = message.playType;
		gameStatus = message.gameStatus;
		message.content = number + ' is sent ' + ' to ' + to + '.';

		//check that manual or automatic
		if (playType === "MANUAL_PLAY") {
			document.getElementById("adjustAbleParamForm").style.display = "block";
			document.getElementById('adjustAbleParamForm').style.visibility = 'visible';
		}
		else {
			document.getElementById("adjustAbleParamForm").style.display = "none";
			document.getElementById('adjustAbleParamForm').style.visibility = 'hidden';
			document.getElementById("sendTheNumber").style.display = "none";
			document.getElementById('sendTheNumber').style.visibility = 'hidden';
		}
		messageElement.classList.add('event-message');
	}

	else if (message.gameStatus === 'ON_GOING') {
		visibleComponents();
		hideButtons();
		gameId = message.gameId;
		gameStatus = message.gameStatus;
		number = message.number;
		playType = message.playType;
		to = message.to;
		move = message.move;
		message.content = number + ' is sent to ' + to + ' (Previous adjustment is ' + move + ')';
		//check that manual or automatic
		if (playType === "MANUAL_PLAY") {
			document.getElementById("adjustAbleParamForm").style.display = "block";
			document.getElementById('adjustAbleParamForm').style.visibility = 'visible';
		}
		else {
			document.getElementById("adjustAbleParamForm").style.display = "none";
			document.getElementById('adjustAbleParamForm').style.visibility = 'hidden';
			document.getElementById("sendTheNumber").style.display = "none";
			document.getElementById('sendTheNumber').style.visibility = 'hidden';
		}
		messageElement.classList.add('event-message');
	}
	else if (message.gameStatus === 'ERROR') {

		visibleComponents();
		//check that manual or automatic
		if (playType === "MANUAL_PLAY") {
			document.getElementById("adjustAbleParamForm").style.display = "block";
			document.getElementById('adjustAbleParamForm').style.visibility = 'visible';
		}
		else {
			document.getElementById("adjustAbleParamForm").style.display = "none";
			document.getElementById('adjustAbleParamForm').style.visibility = 'hidden';
			document.getElementById("sendTheNumber").style.display = "none";
			document.getElementById('sendTheNumber').style.visibility = 'hidden';
		}
		gameId = message.gameId;
		gameStatus = message.gameStatus;
		number = message.number;
		playType = message.playType;
		from = message.from;
		move = message.move;
		message.content = from + ' error in your adjustment : ' + move;
		alert("WARNING: " + from + " : " + " your adjustment: " + move + " is wrong choice")
		messageElement.classList.add('event-message');
	}
	else if (message.gameStatus === 'FINALIZED') {
		hideComponentsExceptMessageArea();
		messageElement.classList.add('event-message');
		gameId = message.gameId;
		gameStatus = message.gameStatus;
		number = message.number;
		playType = message.playType;
		to = message.to;
		move = message.move;
		message.content = to + ' WIN THE GAME';
		//alert(playerName + " Wins the Game")
	}


	messageElement = document.createElement('li');
	textElement = document.createElement('p');
	var messageText = document.createTextNode(message.content);
	textElement.appendChild(messageText);
	messageElement.appendChild(textElement);
	messageArea.appendChild(messageElement);


}


function hideComponents() {
	document.getElementById("sendTheNumber").style.display = "none";
	document.getElementById('sendTheNumber').style.visibility = 'hidden';
	document.getElementById("messageArea").style.display = "none";
	document.getElementById('messageArea').style.visibility = 'hidden';
	document.getElementById("adjustAbleParamForm").style.display = "none";
	document.getElementById('adjustAbleParamForm').style.visibility = 'hidden';
}

function hideComponentsExceptMessageArea() {
	document.getElementById("sendTheNumber").style.display = "none";
	document.getElementById('sendTheNumber').style.visibility = 'hidden';
	document.getElementById("adjustAbleParamForm").style.display = "none";
	document.getElementById('adjustAbleParamForm').style.visibility = 'hidden';
}

function hideButtons() {
	document.getElementById('automaticPlayButton').style.visibility = 'hidden';
	document.getElementById("automaticPlayButton").style.display = "none";
	document.getElementById('manualPlayButton').style.visibility = 'hidden';
	document.getElementById("manualPlayButton").style.display = "none";
}

function visibleComponents() {
	document.getElementById("messageArea").style.display = "block";
	document.getElementById('messageArea').style.visibility = 'visible';
	document.getElementById("sendTheNumber").style.display = "block";
	document.getElementById('sendTheNumber').style.visibility = 'visible';
}

function disconnect(event) {
	if (stompClient) {
		var disconnectMessage = {
			playerId: playerId,
			gameId: gameId,
		};
		stompClient.send("/app/game.disconnectGame", {}, JSON.stringify(disconnectMessage));
	}

	if (stompClient != null) {
		stompClient.disconnect();
	}
	console.log("Disconnected");
	event.preventDefault();
}
usernameForm.addEventListener('submit', connect, true)
adjustAbleParams.addEventListener("change", setMoveValue, false);
manualPlayButton.addEventListener('submit', manualPlay, true)
automaticPlayButton.addEventListener('submit', automaticPlay, true)
sendTheNumberForm.addEventListener('submit', sendTheNumber, true)
disconnectForm.addEventListener('submit', disconnect, true)