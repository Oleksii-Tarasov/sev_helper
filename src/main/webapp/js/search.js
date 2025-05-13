let globalHandleSearchClick;

function debounce(func, wait) {
    let timeout;
    return function () {
        const context = this;
        const args = arguments;
        clearTimeout(timeout);
        timeout = setTimeout(() => {
            func.apply(context, args);
        }, wait);
    };
}

$(document).ready(function () {
    const searchInput = $('#searchInput');
    let currentMatchIndex = -1;
    let matchedCells = [];

    // Preserve the initial state of the rows
    const rows = $('.table tr:not(:first-child)');
    const originalStates = [];

    rows.each(function () {
        originalStates.push({
            element: $(this),
            html: $(this).html(),
            classes: $(this).attr('class') || ''
        });
    });

    function restoreOriginalState() {
        originalStates.forEach(state => {
            state.element.html(state.html);
            state.element.attr('class', state.classes);
        });

        applyRowColors();
        matchedCells = [];
        currentMatchIndex = -1;
    }

    function scrollToMatch(index) {
        if (matchedCells.length === 0) return;

        // Ensure cyclical search
        if (index >= matchedCells.length) {
            index = 0;
        } else if (index < 0) {
            index = matchedCells.length - 1;
        }

        currentMatchIndex = index;

        // Remove the previous active class from all cells
        $('.cell-match-active').removeClass('cell-match-active');

        // Add an active class to the current cell
        const currentCell = matchedCells[currentMatchIndex];
        currentCell.addClass('cell-match-active');

        // Scroll to the current match
        $('html, body').animate({
            scrollTop: currentCell.offset().top - 300
        }, 250);
    }

    function performSearch() {
        const searchText = searchInput.val().trim().toLowerCase();

        if (!searchText) {
            restoreOriginalState();
            return;
        }

        restoreOriginalState();
        matchedCells = [];

        rows.each(function () {
            const row = $(this);
            const cells = row.find('td');

            cells.each(function () {
                const cell = $(this);
                const text = cell.text().toLowerCase();

                if (text.includes(searchText)) {
                    cell.addClass('cell-match');
                    matchedCells.push(cell);
                }
            });
        });

        // Show the number of matches found
        if (matchedCells.length > 0) {
            scrollToMatch(0);
        }
    }

    function handleEnterKey(e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            if (matchedCells.length > 0) {
                scrollToMatch(currentMatchIndex + 1);
            } else {
                performSearch();
            }
        }
    }

    searchInput.on('input', debounce(performSearch, 300));
    searchInput.on('keydown', handleEnterKey);

    // Search Button
    globalHandleSearchClick = function() {
        if (matchedCells.length > 0) {
            scrollToMatch(currentMatchIndex + 1);
        } else {
            performSearch();
        }
    }

    //Autoscroll to top if the search input is empty
    searchInput.on('search', function() {
        if (this.value === '') {
            $('html, body').animate({ scrollTop: 0 }, 250);
        }
    });
});
