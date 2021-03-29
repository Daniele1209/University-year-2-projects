var board;

const Player1 = "O";
const PlayerAi = "X";

const winPositions = [
    [0, 1, 2],
    [3, 4, 5],
    [6, 7, 8],
    [0, 3, 6],
    [1, 4, 7],
    [2, 5, 8],
    [0, 4, 8],
    [6, 4, 2]
]

const cells = document.querySelectorAll(".cell");

function startGame(){
    document.querySelector(".end").style.display = "none";
    //Create an array with 9 elements from 0 to 9
    board = Array.from(Array(9).keys());
    //Remove the X and O from the board when the game restarts
    for (var i = 0; i < cells.length; i++) {
        cells[i].innerText = '';
        cells[i].style.removeProperty("background-color");
        cells[i].addEventListener("click", onClick, false)
    }
}

function onClick(selected_square) {
    // Avoid clicking on the same spot twice
    if (typeof board[selected_square.target.id] == "number") {
        turnMove(selected_square.target.id, Player1)

        if (!checkIfTie())
            turnMove(bestSquare(), PlayerAi);
    }
}
// Map the player movement to the board
function turnMove(square, player) {
    board[square] = player;
    document.getElementById(square).innerText = player;
    // Check if the game ended
    let gameEnd = CheckPositions(board, player);

    if (gameEnd)
        gameOver(gameEnd)
}

function CheckPositions(board, player) {
    let play = board.reduce((a, b, c) => (b === player) ? a.concat(c) : a, []);

    let gameWin = null;
    for (let [index, win] of winPositions.entries()) {
        if (win.every(elem => play.indexOf(elem) > -1)) {
            gameWin = {index: index, player: player};
            break;
        }
    }
    return gameWin;
}

// Stop the game so that the player can not click anymore
function gameOver(gameWin) {
    for (let index of winPositions[gameWin.index]) {
        document.getElementById(index).style.backgroundColor =
            gameWin.player === Player1 ? "blue" : "red";
    }

    for (var i = 0; i < cells.length; i++) {
        cells[i].removeEventListener("click", onClick, false);
    }

    setWinner(gameWin.player === Player1 ? "You Win !" : "You Lose ...")

}

// Basic AI find the best position
function bestSquare() {
    return board.filter(sq => typeof sq == "number")[0];
}

// If there are no empty squares => tie
function checkIfTie() {
    if (board.filter(sq => typeof sq == "number").length === 0) {
        for (var i = 0; i < cells.length; i++) {
            cells[i].style.backgroundColor = "green";
            cells[i].removeEventListener("click", onClick, false);
        }

        setWinner("Tie !");
        return true;
    }
    return false;
}

function setWinner(player) {
    document.querySelector(".end").style.display = "block";
    document.querySelector(".end .text").innerText = player;
}

