$(document).ready(function () {
    // Add a cursor pointer to the column headings being sorted
    $('th[data-sortable="true"]').css('cursor', 'pointer');

    // Handling a click on the title
    $('th[data-sortable="true"]').click(function () {
        const table = $(this).parents('table').eq(0);
        const rows = table.find('tr:gt(0)').toArray();
        const column = $(this).index();
        const ascending = $(this).hasClass('asc');

        // Removing the sort class from other columns
        $(this).siblings('th').removeClass('asc desc');
        $(this).toggleClass('asc desc');

        // Sorting rows
        rows.sort(function (a, b) {
            const A = $(a).find('td').eq(column).text().trim();
            const B = $(b).find('td').eq(column).text().trim();

            // Special logic for "Yes"/"No" columns
            if (A === 'Так' && B === 'Ні') return ascending ? 1 : -1;
            if (A === 'Ні' && B === 'Так') return ascending ? -1 : 1;

            // Number check (for EDRPOU)
            if (!isNaN(A) && !isNaN(B)) {
                return ascending ?
                    parseInt(A) - parseInt(B) :
                    parseInt(B) - parseInt(A);
            }

            // Plain text comparison
            return ascending ?
                A.localeCompare(B, 'uk') :
                B.localeCompare(A, 'uk');
        });

        // Delete all rows except the header and add sorted ones
        table.find('tr:gt(0)').remove();
        table.append(rows);

        // Update the numbering in the first column
        table.find('tr:gt(0)').each(function (index) {
            $(this).find('td:first').text(index + 1);
        });

        // Restoring row colors
        applyRowColors();
    });
});
