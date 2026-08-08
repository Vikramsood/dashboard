import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
type Flight={number:string;route:string;gate:string;departure:string;aircraft:string;status:string;load:number};
type Summary={activeFlights:number;totalBookings:number;averagePassengerLoad:number;totalRevenue:number;delayedFlights:number;onTimeRate:number};
@Component({selector:'app-root',standalone:true,imports:[CommonModule],templateUrl:'./app.component.html',styleUrl:'./app.component.css'})
export class AppComponent {
  active='Dashboard'; view:'dashboard'|'flights'='dashboard'; selectedFlight?:Flight; isAddFlightOpen=false; loading=true; apiError=''; flights:Flight[]=[]; summary?:Summary;
  nav=['Dashboard','Flights','Check-in','Boarding','Crew','Passengers','Reports'];
  constructor(private http:HttpClient){this.loadDashboard();}
  loadDashboard(){this.loading=true;this.apiError='';this.http.get<any[]>('http://localhost:8081/api/dashboard/flights').subscribe({next:items=>{this.flights=items.map(x=>({number:x.flightNumber,route:`${x.origin} → ${x.destination}`,gate:x.gate,departure:new Date(x.scheduledDeparture).toLocaleTimeString([],{hour:'2-digit',minute:'2-digit'}),aircraft:x.aircraft,status:this.label(x.status),load:x.passengerLoad}));this.http.get<Summary>('http://localhost:8081/api/dashboard/summary').subscribe({next:summary=>{this.summary=summary;this.loading=false;},error:()=>this.fail()});},error:()=>this.fail()});}
  fail(){this.loading=false;this.apiError='API unavailable. Start Spring Boot and PostgreSQL to load dashboard data.';}
  selectNav(item:string){this.active=item;this.view=item==='Flights'?'flights':'dashboard';} back(){this.view='dashboard';this.active='Dashboard';} openFlight(f:Flight){this.selectedFlight=f;} closeModal(){this.selectedFlight=undefined;this.isAddFlightOpen=false;} addFlight(){this.isAddFlightOpen=true;} private label(s:string){return s.split('_').map(x=>x[0]+x.slice(1).toLowerCase()).join(' ');}
}
