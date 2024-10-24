<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>구니알람</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@300;400;500&display=swap');
        
        body {
            font-family: 'Roboto', -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Helvetica Neue', Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #F2F2F7;
            color: #1C1C1E;
        }
        .container {
            max-width: 400px;
            margin: 0 auto;
            background-color: #FFFFFF;
            border-radius: 12px;
            padding: 24px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        h1 {
            text-align: center;
            color: #1C1C1E;
            font-weight: 500;
            margin-bottom: 24px;
        }
        #timerDisplay {
            font-size: 48px;
            text-align: center;
            margin-bottom: 24px;
            font-weight: 300;
            color: #007AFF;
        }
        .buttons {
            display: flex;
            justify-content: space-between;
            margin-bottom: 24px;
            flex-wrap: wrap;
        }
        .buttons button {
            padding: 12px 16px;
            font-size: 16px;
            border: none;
            border-radius: 8px;
            background-color: #E5E5EA;
            color: #007AFF;
            cursor: pointer;
            flex-basis: calc(33.333% - 8px);
            margin-bottom: 8px;
            font-weight: 500;
            transition: all 0.2s;
        }
        .buttons button:hover {
            background-color: #D1D1D6;
        }
        #btnAdd {
            background-color: #007AFF;
            color: #FFFFFF;
        }
        #btnAdd:hover {
            background-color: #0056B3;
        }
        #customTimeInput {
            width: 100%;
            padding: 12px;
            font-size: 16px;
            margin-bottom: 16px;
            box-sizing: border-box;
            display: none;
            border: 1px solid #D1D1D6;
            border-radius: 8px;
            outline: none;
        }
        #customTimeInput:focus {
            border-color: #007AFF;
        }
        #queueList {
            list-style-type: none;
            padding: 0;
            margin-bottom: 24px;
        }
        #queueList li {
            background-color: #F2F2F7;
            margin-bottom: 8px;
            padding: 12px 16px;
            border-radius: 8px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-weight: 400;
        }
        #queueList li.active {
            background-color: #E5F2FF;
            border: 1px solid #007AFF;
            color: #007AFF;
        }
        .removeBtn {
            background-color: transparent;
            color: #FF3B30;
            border: none;
            padding: 4px 8px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 14px;
            font-weight: 500;
        }
        .removeBtn:hover {
            background-color: #FFD1D1;
        }
        #startButton, #clearQueueButton {
            display: block;
            width: 100%;
            padding: 16px;
            font-size: 18px;
            margin-top: 16px;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 500;
            transition: background-color 0.2s;
        }
        #startButton {
            background-color: #34C759;
            color: #FFFFFF;
        }
        #startButton:hover {
            background-color: #2DA44E;
        }
        #clearQueueButton {
            background-color: #FF3B30;
            color: #FFFFFF;
        }
        #clearQueueButton:hover {
            background-color: #D63028;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>구니알람</h1>
        <div id="timerDisplay">00:00</div>
        <div class="buttons">
            <button id="btn5">5초</button>
            <button id="btn10">10초</button>
            <button id="btn30">30초</button>
            <button id="btn60">1분</button>
            <button id="btnCustom">입력</button>
            <button id="btnAdd">추가</button>
        </div>
        <input type="number" id="customTimeInput" placeholder="초 단위로 시간 입력">
        <ul id="queueList"></ul>
        <button id="startButton">시작</button>
        <button id="clearQueueButton">큐 초기화</button>
    </div>

    <script>
    $(document).ready(function() {
        var currentTime = 0;
        var queue = [];
        var isRunning = false;
        var currentQueueIndex = -1;
        var audio = new Audio('/sound/alarm.mp3');

        function updateTimerDisplay(time) {
            var minutes = Math.floor(time / 60);
            var seconds = time % 60;
            $('#timerDisplay').text(
                (minutes < 10 ? '0' : '') + minutes + ':' +
                (seconds < 10 ? '0' : '') + seconds
            );
        }

        function addTime(seconds) {
            currentTime += seconds;
            updateTimerDisplay(currentTime);
        }

        $('#btn5').click(function() { addTime(5); });
        $('#btn10').click(function() { addTime(10); });
        $('#btn30').click(function() { addTime(30); });
        $('#btn60').click(function() { addTime(60); });

        $('#btnCustom').click(function() {
            $('#customTimeInput').toggle();
        });

        $('#btnAdd').click(function() {
            var timeToAdd = currentTime;
            if ($('#customTimeInput').is(':visible')) {
                timeToAdd = parseInt($('#customTimeInput').val());
                if (isNaN(timeToAdd) || timeToAdd <= 0) {
                    alert('올바른 시간을 입력해주세요.');
                    return;
                }
                $('#customTimeInput').val('').hide();
            }
            if (timeToAdd > 0) {
                queue.push(timeToAdd);
                $('#queueList').append('<li data-time="' + timeToAdd + '"><span>' + timeToAdd + '초</span><button class="removeBtn">X</button></li>');
                currentTime = 0;
                updateTimerDisplay(currentTime);
            }
        });

        $(document).on('click', '.removeBtn', function() {
            var index = $(this).parent().index();
            queue.splice(index, 1);
            $(this).parent().remove();
            if (isRunning && index <= currentQueueIndex) {
                currentQueueIndex--;
            }
        });

        function runQueue() {
            if (currentQueueIndex >= queue.length) {
                isRunning = false;
                currentQueueIndex = -1;
                $('#queueList li').removeClass('active');
                alert('모든 알람이 종료되었습니다.');
                return;
            }

            var time = queue[currentQueueIndex];
            $('#queueList li').removeClass('active');
            $('#queueList li:eq(' + currentQueueIndex + ')').addClass('active');

            function countdown() {
                updateTimerDisplay(time);
                if (time <= 0) {
                    audio.play();
                    currentQueueIndex++;
                    setTimeout(runQueue, 1000); // 1초 후 다음 큐 실행
                } else {
                    time--;
                    setTimeout(countdown, 1000);
                }
            }

            countdown();
        }

        $('#startButton').click(function() {
            if (!isRunning && queue.length > 0) {
                isRunning = true;
                currentQueueIndex = 0;
                runQueue();
            }
        });

        $('#clearQueueButton').click(function() {
            queue = [];
            $('#queueList').empty();
            currentTime = 0;
            updateTimerDisplay(currentTime);
            isRunning = false;
            currentQueueIndex = -1;
        });
    });
    </script>
</body>
</html>