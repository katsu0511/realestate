$(function() {
	// SheetをWorkbookに追加する
	// 参照：https://github.com/SheetJS/js-xlsx/issues/163
	function sheet_to_workbook(sheet/*:Worksheet*/, opts)/*:Workbook*/ {
		var n = opts && opts.sheet ? opts.sheet : "Sheet1";
		var sheets = {}; sheets[n] = sheet;
		return { SheetNames: [n], Sheets: sheets };
	}
	
	// ArrayをWorkbookに変換する
	// 参照：https://github.com/SheetJS/js-xlsx/issues/163
	function aoa_to_workbook(data/*:Array<Array<any> >*/, opts)/*:Workbook*/ {
		return sheet_to_workbook(XLSX.utils.aoa_to_sheet(data, opts), opts);
	}
	
	// stringをArrayBufferに変換する
	// 参照：https://stackoverflow.com/questions/34993292/how-to-save-xlsx-data-to-file-as-a-blob
	function s2ab(s) {
		var buf = new ArrayBuffer(s.length);
		var view = new Uint8Array(buf);
		for (var i = 0; i != s.length; ++i) view[i] = s.charCodeAt(i) & 0xFF;
			return buf;
	}
	
	$('#submit').click(function() {
		if (confirm('入金消込情報をエクスポートしますか？')) {
			//出力するオブジェクト(Array)
			var table1 = $('.display-thead .display-th').map(function() {
							const displayTh = $.trim($(this).text());
							return displayTh
						});
			var table2 = $('.display .display-tbody.visible').map(function() {
							return $(this).find('.display-td').map(function() {
								const displayTd = $.trim($(this).text());
								return displayTd
							});
						});
			var array1 = table1.toArray().join(',') + ',' + table2.map(function(i, row){return row.toArray().join(',');}).toArray().join(',');
			var array2 = array1.split(',');
			var array3 = [];
			var array4 = [];
			for (i = 0; i < array2.length; i++) {
				array3.push(array2[i]);
				if (array3.length == 7) {
					array4.push(array3);
					array3 = [];
				}
			}
			
			if (array4.length > 0) {
				// 書き込み時のオプションは以下を参照
				// https://github.com/SheetJS/js-xlsx/blob/master/README.md#writing-options
				var write_opts = {
					type: 'binary'
				};
				
				// ArrayをWorkbookに変換する
				var wb = aoa_to_workbook(array4);
				var wb_out = XLSX.write(wb, write_opts);
				
				// WorkbookからBlobオブジェクトを生成
				// 参照：https://developer.mozilla.org/ja/docs/Web/API/Blob
				var blob = new Blob([s2ab(wb_out)], {type: 'application/octet-stream'});
				
				// FileSaverのsaveAs関数で、xlsxファイルとしてダウンロード
				// 参照：https://github.com/eligrey/FileSaver.js/
				saveAs(blob, 'paymentInfo.xlsx');
			} else {
				alert('入金消込情報がありません');
			}
		};
	});
});
