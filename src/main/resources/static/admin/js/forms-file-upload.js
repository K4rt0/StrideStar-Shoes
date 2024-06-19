"use strict";

(function() {
    var previewTemplate = `<div class="dz-preview dz-file-preview">
        <div class="dz-details">
            <div class="dz-thumbnail">
                <img data-dz-thumbnail>
                <span class="dz-nopreview">No preview</span>
                <div class="dz-success-mark"></div>
                <div class="dz-error-mark"></div>
                <div class="dz-error-message"><span data-dz-errormessage></span></div>
                <div class="progress">
                    <div class="progress-bar progress-bar-primary" role="progressbar" aria-valuemin="0" aria-valuemax="100" data-dz-uploadprogress></div>
                </div>
            </div>
            <div class="dz-filename" data-dz-name></div>
            <div class="dz-size" data-dz-size></div>
        </div>
    </div>`;

    var dropzoneConfigs = {
        "#dropzone-basic": {
            maxFiles: 1,
            parallelUploads: 1
        },
        "#dropzone-multi": {
            maxFiles: null, // No limit
            parallelUploads: 1
        }
    };

    function initializeDropzones() {
        document.querySelectorAll(".dropzone").forEach(dropzoneElement => {
            var inputElement = dropzoneElement.querySelector("input[type='file']");
            var configKey = `#${dropzoneElement.id}`;
            var config = dropzoneConfigs[configKey];
            var acceptedFiles = dropzoneElement.dataset.type === "images" ? "image/*" : null;
            var files = [];

            if (dropzoneElement && inputElement && config) {
                var myDropzone = new Dropzone(dropzoneElement, {
                    url: "/upload",
                    previewTemplate: previewTemplate,
                    parallelUploads: config.parallelUploads,
                    maxFilesize: 5,
                    addRemoveLinks: true,
                    maxFiles: config.maxFiles, // This will be `null` for no limit
                    acceptedFiles: acceptedFiles, // Accept specific file types based on data-type
                    autoProcessQueue: true,
                    init: function() {
                        this.on("addedfile", function(file) {
                            files.push(file);
                            updateInputFiles(inputElement, files);
                        });
                        this.on("removedfile", function(file) {
                            files = files.filter(f => f !== file);
                            updateInputFiles(inputElement, files);
                        });
                    }
                });

                // Store a reference to the Dropzone instance in the element's dataset
                dropzoneElement.dataset.dropzoneInstance = myDropzone;
            }
        });
    }

    function updateInputFiles(inputElement, files) {
        var dataTransfer = new DataTransfer();
        files.forEach(file => dataTransfer.items.add(file));
        inputElement.files = dataTransfer.files;
    }

    // Initialize all dropzones on page load
    document.addEventListener("DOMContentLoaded", initializeDropzones);
})();
