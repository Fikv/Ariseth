import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ChartConfiguration, ChartType } from 'chart.js';
import { NgChartsModule } from 'ng2-charts';
import { NgIf } from '@angular/common';

@Component({
  selector: 'app-crypto',
  standalone: true,
  templateUrl: './crypto.component.html',
  imports: [
    NgChartsModule,
    NgIf
  ],
  styleUrl: './crypto.component.css'
})
export class CryptoComponent implements OnInit {
  public lineChartType: ChartType = 'line';
  public lineChartData: ChartConfiguration['data'] = {
    labels: [],
    datasets: [
      {
        data: [],
        label: 'BTC/PLN',
        tension: 0.4,
        fill: true
      }
    ]
  };
  public lineChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: {
      legend: { display: false }
    }
  };

  loadingChart = true;

  username: string = '';
  userRole: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchChart();
    this.fetchUserInfo();
  }

  fetchUserInfo() {
    // Read credentials from localStorage (set after login)
    const credentials = localStorage.getItem('basicAuth');
    if (!credentials) {
      this.username = 'Guest';
      this.userRole = '';
      return;
    }
    const headers = new HttpHeaders({
      Authorization: credentials
    });
    this.http.get<any>('http://localhost:8080/api/me', { headers }).subscribe({
      next: (res) => {
        this.username = res.username;
        // roles is usually an array, e.g., ["ROLE_ADMIN"], show first or join all
        this.userRole = res.roles && res.roles.length ? res.roles[0] : '';
      },
      error: (err) => {
        this.username = 'Guest';
        this.userRole = '';
      }
    });
  }

  fetchChart() {
    this.loadingChart = true;
    this.http.get<any>(
      'https://api.coingecko.com/api/v3/coins/bitcoin/market_chart?vs_currency=pln&days=7'
    ).subscribe(data => {
      const labels = data.prices.map((item: any) => {
        const d = new Date(item[0]);
        return `${d.getDate()}.${d.getMonth() + 1}`;
      });
      const prices = data.prices.map((item: any) => item[1]);

      this.lineChartData = {
        labels: labels,
        datasets: [
          {
            data: prices,
            label: 'BTC/PLN',
            tension: 0.4,
            borderColor: '#286ae6',
            backgroundColor: 'rgba(40,106,230,0.09)',
            pointBackgroundColor: '#286ae6',
            fill: true
          }
        ]
      };
      this.loadingChart = false;
    });
  }
}
