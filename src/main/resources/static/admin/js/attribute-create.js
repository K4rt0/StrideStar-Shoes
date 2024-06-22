$(document).ready(function() {
    let attributeValues = [];

    $('#createAttribute').click(function() {
        const name = $('#attributeName').val();
        if (name) {
            const productAttribute = {
                name: name,
                productAttributeValues: attributeValues.map(value => ({ value: value }))
            };
            /* $.ajax({
                url: '/admin/attributes/create',
                method: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(productAttribute),
                success: function(response) {
                    window.location.href = '/admin/attributes';
                },
                error: function(error) {
                    alert('Lỗi khi tạo thuộc tính!');
                    console.error(error); // Thêm log lỗi để kiểm tra
                }
            }); */
        }
        else
        {
            showIziToast('Cảnh báo', 'Bạn chưa nhập tên thuộc tính !', 'yellow');
        }
    });

    $('#addAttributeValue').click(function() {
        const value = $('#attributeValue').val();
        if (value) {
            attributeValues.push(value);
            $('#attributeValuesTable tbody').append(
                '<tr>' +
                '<td class="value-cell">' + value + '</td>' +
                '<td>' +
                '<button class="btn btn-warning btn-sm editValue" data-value="' + value + '">Sửa</button> ' +
                '<button class="btn btn-danger btn-sm removeValue" data-value="' + value + '">Xoá</button>' +
                '</td>' +
                '</tr>'
            );
            $('#attributeValue').val('');
        }
        else
        {
            showIziToast('Cảnh báo', 'Bạn chưa nhập giá trị thuộc tính !', 'yellow');
        }
    });

    $(document).on('click', '.removeValue', function() {
        const value = $(this).data('value');
        attributeValues = attributeValues.filter(v => v !== value);
        $(this).closest('tr').remove();
    });

    $(document).on('click', '.editValue', function() {
        const value = $(this).data('value');
        const $row = $(this).closest('tr');
        const $valueCell = $row.find('.value-cell');
        const newValue = prompt("Sửa giá trị thuộc tính:", value);
        if (newValue) {
            const index = attributeValues.indexOf(value);
            if (index !== -1) {
                attributeValues[index] = newValue;
                $valueCell.text(newValue);
                $(this).data('value', newValue);
                $row.find('.removeValue').data('value', newValue);
            }
        }
    });
});