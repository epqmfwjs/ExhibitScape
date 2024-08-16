google.charts.load('current', {'packages':['corechart']});
google.charts.setOnLoadCallback(drawCharts);

function drawCharts() {
    drawPieChart();
    drawLineChart();
}

function drawPieChart() {
    var data = google.visualization.arrayToDataTable([
        ['게시판', '게시글 수'],
        ['커뮤니티', /*[[${boardCounts['Community']}]]*/],
        ['갤러리', /*[[${boardCounts['Gallery']}]]*/]
    ]);

    var options = {
        title: '게시판별 게시글 수',
        is3D: true
    };

    var chart = new google.visualization.PieChart(document.getElementById('pie_chart_div'));
    chart.draw(data, options);
}

function drawLineChart() {
    var data = new google.visualization.DataTable();
    data.addColumn('string', '날짜');
    data.addColumn('number', '커뮤니티');

    var communityCounts = /*[[${communityCounts}]]*/;

    var dates = Object.keys(communityCounts).sort();
    for (var i = 0; i < dates.length; i++) {
        var date = dates[i];
        var count = communityCounts[date];
        data.addRow([date, count]);
    }

    var options = {
        title: '커뮤니티 게시판 날짜별 게시글 수',
        curveType: 'function',
        legend: { position: 'bottom' }
    };

    var chart = new google.visualization.LineChart(document.getElementById('line_chart_div'));
    chart.draw(data, options);
}
