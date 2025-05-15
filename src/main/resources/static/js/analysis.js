// Analysis visualization module

document.addEventListener('DOMContentLoaded', function() {
    // Initialize form submission
    const analysisForm = document.getElementById('analysisForm');
    if (analysisForm) {
        analysisForm.addEventListener('submit', handleAnalysisSubmit);
    }

    // Setup the chart containers
    setupChartContainers();
});

function setupChartContainers() {
    const chartContainer = document.getElementById('chartContainer');
    if (!chartContainer) return;

    chartContainer.innerHTML = `
        <div class="row mt-4">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5>Compression Ratio vs. K Values</h5>
                    </div>
                    <div class="card-body">
                        <canvas id="compressionRatioChart"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5>PSNR vs. K Values</h5>
                    </div>
                    <div class="card-body">
                        <canvas id="psnrChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
        <div class="row mt-4">
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5>Processing Time vs. K Values</h5>
                    </div>
                    <div class="card-body">
                        <canvas id="processingTimeChart"></canvas>
                    </div>
                </div>
            </div>
            <div class="col-md-6">
                <div class="card">
                    <div class="card-header">
                        <h5>Color Reduction vs. K Values</h5>
                    </div>
                    <div class="card-body">
                        <canvas id="colorReductionChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
        <div class="row mt-4">
            <div class="col-12">
                <div class="card">
                    <div class="card-header">
                        <h5>Analysis Results Table</h5>
                    </div>
                    <div class="card-body">
                        <table id="resultsTable" class="table table-striped">
                            <thead>
                                <tr>
                                    <th>K Value</th>
                                    <th>Compression Ratio</th>
                                    <th>PSNR (dB)</th>
                                    <th>Processing Time (ms)</th>
                                    <th>Color Reduction (%)</th>
                                    <th>File Size (KB)</th>
                                </tr>
                            </thead>
                            <tbody>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    `;
}

async function handleAnalysisSubmit(event) {
    event.preventDefault();
    
    const form = event.target;
    const formData = new FormData(form);
    
    const submitButton = form.querySelector('button[type="submit"]');
    const originalButtonText = submitButton.textContent;
    submitButton.textContent = 'Processing...';
    submitButton.disabled = true;
    
    const resultDiv = document.getElementById('analysisResult');
    resultDiv.innerHTML = '<div class="alert alert-info">Analyzing image with K-means compression...</div>';
    
    try {
        const response = await fetch('/api/analysis/compress', {
            method: 'POST',
            body: formData
        });
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        
        const data = await response.json();
        displayResults(data);
    } catch (error) {
        resultDiv.innerHTML = `<div class="alert alert-danger">Error: ${error.message}</div>`;
    } finally {
        submitButton.textContent = originalButtonText;
        submitButton.disabled = false;
    }
}

function displayResults(data) {
    const resultDiv = document.getElementById('analysisResult');
    resultDiv.innerHTML = '';
    
    // Original image info
    resultDiv.innerHTML = `
        <div class="alert alert-success">
            Analysis completed successfully!
        </div>
        <div class="card mb-4">
            <div class="card-header">
                <h5>Original Image Information</h5>
            </div>
            <div class="card-body">
                <p><strong>Dimensions:</strong> ${data.originalDimensions.width} × ${data.originalDimensions.height}</p>
                <p><strong>File Size:</strong> ${(data.originalSize / 1024).toFixed(2)} KB</p>
                <p><strong>Unique Colors:</strong> ${data.compressionResults[0].originalColors}</p>
            </div>
        </div>
    `;
    
    // Extract data for charts
    const kValues = [];
    const compressionRatios = [];
    const psnrValues = [];
    const processingTimes = [];
    const colorReductions = [];
    
    // Populate table and collect data for charts
    const tableBody = document.querySelector('#resultsTable tbody');
    tableBody.innerHTML = '';
    
    data.compressionResults.forEach(result => {
        kValues.push(result.k);
        compressionRatios.push(result.compressionRatio);
        psnrValues.push(result.psnr);
        processingTimes.push(result.processingTimeMs);
        colorReductions.push(result.colorReduction);
        
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${result.k}</td>
            <td>${result.compressionRatio.toFixed(2)}</td>
            <td>${result.psnr.toFixed(2)}</td>
            <td>${result.processingTimeMs}</td>
            <td>${result.colorReduction.toFixed(2)}%</td>
            <td>${(result.compressedSize / 1024).toFixed(2)}</td>
        `;
        tableBody.appendChild(row);
    });
    
    // Create charts
    createCompressionRatioChart(kValues, compressionRatios);
    createPSNRChart(kValues, psnrValues);
    createProcessingTimeChart(kValues, processingTimes);
    createColorReductionChart(kValues, colorReductions);
}

function createCompressionRatioChart(kValues, compressionRatios) {
    const ctx = document.getElementById('compressionRatioChart').getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: kValues,
            datasets: [{
                label: 'Compression Ratio',
                data: compressionRatios,
                backgroundColor: 'rgba(54, 162, 235, 0.2)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 2,
                pointRadius: 5,
                tension: 0.1
            }]
        },
        options: {
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'K Value (Number of Colors)'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Compression Ratio'
                    },
                    min: 0
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return `Compression Ratio: ${context.parsed.y.toFixed(2)}`;
                        }
                    }
                }
            }
        }
    });
}

function createPSNRChart(kValues, psnrValues) {
    const ctx = document.getElementById('psnrChart').getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: kValues,
            datasets: [{
                label: 'PSNR (dB)',
                data: psnrValues,
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderColor: 'rgba(75, 192, 192, 1)',
                borderWidth: 2,
                pointRadius: 5,
                tension: 0.1
            }]
        },
        options: {
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'K Value (Number of Colors)'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'PSNR (dB)'
                    },
                    min: 0
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return `PSNR: ${context.parsed.y.toFixed(2)} dB`;
                        }
                    }
                }
            }
        }
    });
}

function createProcessingTimeChart(kValues, processingTimes) {
    const ctx = document.getElementById('processingTimeChart').getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: kValues,
            datasets: [{
                label: 'Processing Time (ms)',
                data: processingTimes,
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                borderColor: 'rgba(255, 99, 132, 1)',
                borderWidth: 2,
                pointRadius: 5,
                tension: 0.1
            }]
        },
        options: {
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'K Value (Number of Colors)'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Processing Time (ms)'
                    },
                    min: 0
                }
            }
        }
    });
}

function createColorReductionChart(kValues, colorReductions) {
    const ctx = document.getElementById('colorReductionChart').getContext('2d');
    new Chart(ctx, {
        type: 'line',
        data: {
            labels: kValues,
            datasets: [{
                label: 'Color Reduction (%)',
                data: colorReductions,
                backgroundColor: 'rgba(153, 102, 255, 0.2)',
                borderColor: 'rgba(153, 102, 255, 1)',
                borderWidth: 2,
                pointRadius: 5,
                tension: 0.1
            }]
        },
        options: {
            scales: {
                x: {
                    title: {
                        display: true,
                        text: 'K Value (Number of Colors)'
                    }
                },
                y: {
                    title: {
                        display: true,
                        text: 'Color Reduction (%)'
                    },
                    min: 0,
                    max: 100
                }
            },
            plugins: {
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            return `Color Reduction: ${context.parsed.y.toFixed(2)}%`;
                        }
                    }
                }
            }
        }
    });
} 